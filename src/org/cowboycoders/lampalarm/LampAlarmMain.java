package org.cowboycoders.lampalarm;

import java.util.ArrayList;
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
	
	private int[] logo = {0x0,0x0,0x53,0x60,0xe0,0xe0,0x3,0xf0,0xf8,0xfe,0xff,0xf8,0xf0,0xe0,0xe0,0x3,0x60,0x0,0x0,0x26,0x80,0x80,0x4,0xc0,0xe0,0xf8,0xfc,0xe0,0xc0,0x80,0x80,0x4,0x0,0x0,0x1f,0x80,0xc0,0x0,0x0,0x1f,0x61,0x3f,0x1f,0x1f,0x2,0xf,0xf,0x2,0x1f,0x3f,0x3f,0x2,0x61,0x0,0x0,0x28,0x1,0x3,0x87,0xff,0x7f,0x7f,0x2,0x3f,0x3f,0x2,0x7f,0xff,0xff,0x2,0x87,0x3,0x1,0x0,0x0,0x19,0x18,0x38,0x78,0xf8,0xfc,0xfe,0xff,0xff,0x2,0xfe,0xfc,0xf8,0x78,0x38,0x18,0x0,0x0,0x35,0x80,0xc0,0x0,0x0,0x16,0x1,0x0,0x0,0x8,0x1,0x0,0x0,0xc,0x10,0xf0,0x0,0x0,0xf,0x18,0xf,0x7,0x7,0x2,0x3,0x3,0x2,0x7,0xf,0xf,0x2,0x18,0x0,0x0,0x14,0x10,0xf0,0x0,0x0,0x1b,0x18,0x38,0x78,0xf8,0xfc,0xfe,0xff,0xff,0x2,0xfe,0xfc,0xf8,0x78,0x38,0x18,0x0,0x0,0xb,0x3e,0x61,0x41,0x41,0x2,0x43,0x0,0x0,0x2,0x3e,0x41,0x41,0x4,0x3e,0x0,0x0,0x2,0x1,0x7,0x3d,0x60,0x10,0x6,0xe,0x38,0x60,0x18,0x2,0x0,0x0,0x2,0x7f,0x42,0x41,0x41,0x3,0x23,0x1e,0x0,0x0,0x2,0x3e,0x41,0x41,0x4,0x3e,0x0,0x0,0x2,0x1,0x3,0xf,0xf0,0x60,0x8,0x3,0x1,0x0,0x3e,0x61,0x41,0x41,0x2,0x43,0x0,0x0,0x2,0x3e,0x41,0x41,0x4,0x3e,0x0,0x0,0x3,0x3e,0x63,0x41,0x41,0x2,0x1,0x7f,0x40,0x0,0x0,0x2,0x3e,0x65,0x45,0x45,0x3,0x26,0x0,0x0,0x2,0x41,0x7f,0x42,0x3,0x1,0x0,0x66,0x4d,0x49,0x59,0x30,0x0,0x0,0x7,0x18,0xf,0x7,0x7,0x2,0x3,0x3,0x2,0x7,0xf,0xf,0x2,0x18,0x0,0x0,0x22,0x80,0xc0,0x0,0x0,0x17,0x2,0x2,0x2,0x1,0x0,0x0,0x21,0x80,0xc0,0x0,0x0,0x3b,0x18,0x38,0x78,0xf8,0xfc,0xfe,0xff,0xff,0x2,0xfe,0xfc,0xf8,0x78,0x38,0x18,0x0,0x0,0x2f,0x18,0x38,0x78,0xf8,0xfc,0xfe,0xff,0xff,0x2,0xfe,0xfc,0xf8,0x78,0x38,0x18,0x0,0x0,0x16,0x80,0xc0,0xf0,0xf8,0xc0,0x80,0x0,0x0,0x1b,0x18,0xf,0x7,0x7,0x2,0x3,0x3,0x2,0x7,0xf,0xf,0x2,0x18,0x0,0x0,0x33,0x18,0xf,0x7,0x7,0x2,0x3,0x3,0x2,0x7,0xf,0xf,0x2,0x18,0x0,0x0,0x14,0x3,0x7,0xf,0xff,0xff,0x3,0x7f,0x7f,0x2,0xff,0xff,0x3,0xf,0x7,0x3};
	
	//Debug
	private static final String TAG = "LampAlarm";
	private static final boolean D = true;
	
	// Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    
    //Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;
    
 // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    private ListView mConversationView;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
 // String buffer for incomingg messages
    private StringBuffer mInStringBuffer;
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
        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);
        
        // Make a message button
 		Button button = (Button) findViewById(R.id.button1);
 		button.setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				//Toast.makeText(LampAlarmMain.this, "Test", Toast.LENGTH_SHORT).show();
 				String msg = "HELLO WORLD\n";
 				sendMessage(msg);
 				//Toast.makeText(LampAlarmMain.this, "SENT", Toast.LENGTH_SHORT).show();
 		        
 			}
 		});
 		
        // Initialize the BluetoothMessageService to perform bluetooth connections
        mLampAlarmService = new BluetoothMessageService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
        
        // Initialize the buffer for incoming messages
        //mInStringBuffer = new StringBuffer("");
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

            // Reset out string buffer to zero and clear the edit text field
            //mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

	
	   private final void setStatus(int resId) {
	        final ActionBar actionBar = getActionBar();
	        actionBar.setSubtitle(resId);
	    }

	    private final void setStatus(CharSequence subTitle) {
	        final ActionBar actionBar = getActionBar();
	        actionBar.setSubtitle(subTitle);
	    }
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothMessageService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothMessageService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BluetoothMessageService.STATE_LISTEN:
                case BluetoothMessageService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                //byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                // The buffer length is 1024
                //String readMessage = new String(readBuf, 0, msg.arg1);
            	
            	
            	List<Integer> frame= (List<Integer>) msg.obj;
            	
            	/*
            	List<Integer> frame = new ArrayList<Integer>();
            	for (int i = 0; i < logo.length; ++i) {
            		frame.add(logo[i]);
            	} 
            	
            	for (int i = 0; i < 10; ++i) {
            		if(D) Log.i(TAG, "Element compare: " + frame.get(i) + "  orig: " + frame2.get(i));
            	}
            	
            	if(D) Log.i(TAG, "Size compare: " + frame.size() + "  orig: " + frame2.size());
            	
                if(D) Log.i(TAG, "Message arrived back: " + frame.size()); */
                
                BitmapGenerator bGen = new BitmapGenerator(128, 64);
        	    Bitmap b = bGen.byteArrayToBitmap(frame);
        	    button.setImageBitmap(b);
        	    button = (ImageView) findViewById(R.id.image);
        	    
                // Append the message to the string buffer
                //mInStringBuffer.append(readMessage);
                //TODO pass the string buffer to a pars
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + mInStringBuffer);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
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

}
