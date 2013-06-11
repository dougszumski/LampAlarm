package org.cowboycoders.lampalarm;

import java.util.List;




import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class LampAlarmMain extends Activity {
	
	//Debug
	private static final String TAG = "LampAlarm";
	private static final boolean D = true;
	
	// Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    
    //Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    
    
    private ArrayAdapter<String> mConversationArrayAdapter;
    
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;

    // Member object for the chat services
    private BluetoothMessageService mLampAlarmService = null;
    
    // Image to be displayed
    ImageView button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(D) Log.e(TAG, "+++ ON CREATE +++");
		
		//Request action bar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        // Set up the window layout
        setContentView(R.layout.activity_lamp_alarm_main);
        
        button = (ImageView) findViewById(R.id.image);
		
		// Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        //Setup menu
        setContentView(R.layout.activity_lamp_alarm_main);
        
        
	}
	
	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
        
        //Turn on BT if not enabled
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
        	if (mLampAlarmService == null) setupLampAlarmSession();
        }
    }
	
	private void setupLampAlarmSession() {
        Log.d(TAG, "setupLampAlarmSession()");

        // Initialize the array adapter for the conversation thread
        setmConversationArrayAdapter(new ArrayAdapter<String>(this, R.layout.message));
      
        
        // Left button
 		Button button1 = (Button) findViewById(R.id.button1);
 		button1.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				String msg = ":a:";
 				sendMessage(msg);
 			}
 		});
 		
		// Up button
 		Button button2 = (Button) findViewById(R.id.button2);
 		button2.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				String msg = ":s:";
 				sendMessage(msg);
 			}
 		});
 		
 		// Right button
 		Button button3 = (Button) findViewById(R.id.button3);
 		button3.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				String msg = ":d:";
 				sendMessage(msg);
 			}
 		});
 		
        // Initialize the BluetoothMessageService to perform bluetooth connections
 	 	MessageHandler mHandler = new MessageHandler(this);
        mLampAlarmService = new BluetoothMessageService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
        
    }
	
	/**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mLampAlarmService.getState() != BluetoothMessageService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mLampAlarmService.write(send);
        }
    }

	//Gets called after an activity returns
	public void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a session
                setupLampAlarmSession();
            } else {
                // User did not enable Bluetooth or an error occurred
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

	private void connectDevice(Intent data) {
        // Get the device MAC address
        String address = data.getExtras()
            .getString(BluetoothConfigure.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mLampAlarmService.connect(device);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lamp_alarm_main, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent serverIntent = null;
        switch (item.getItemId()) {
        case R.id.action_connect:
            // Launch the DeviceListActivity to see devices and do scan
        	// If we connect we should get back the mac in the device intent
            serverIntent = new Intent(this, BluetoothConfigure.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return true;
        case R.id.action_settings:
            // TODO: Implement a settings menu
            return true;
        }
        return false;
    }

	/**
	 * Updates the image on the screen.
	 * 
	 * @param frame - Frame to update image with.
	 */
	public void updateImage(List<Integer> frame) {
		if(D) Log.e(TAG, "++ ON HERE!!!!!!!! ++");
		BitmapGenerator bitmapGenerator = new BitmapGenerator(128, 64);
		Bitmap frameBuffer = bitmapGenerator.byteArrayToBitmap(frame);
	    button.setImageBitmap(frameBuffer);
	    button = (ImageView) findViewById(R.id.image);
	}

	public ArrayAdapter<String> getmConversationArrayAdapter() {
		return mConversationArrayAdapter;
	}

	public void setmConversationArrayAdapter(
			ArrayAdapter<String> mConversationArrayAdapter) {
		this.mConversationArrayAdapter = mConversationArrayAdapter;
	}

}
