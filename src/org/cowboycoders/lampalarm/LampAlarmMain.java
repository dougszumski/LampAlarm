package org.cowboycoders.lampalarm;

import java.util.List;

import org.cowboycoders.lampalarm.backend.BitmapGenerator;
import org.cowboycoders.lampalarm.backend.BluetoothConfigure;
import org.cowboycoders.lampalarm.backend.BluetoothMessageService;
import org.cowboycoders.lampalarm.backend.MessageHandler;
import org.cowboycoders.lampalarm.fragments.FragmentPager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class LampAlarmMain extends FragmentActivity implements
	ActionBar.TabListener {

    // Debug
    private static final String TAG = "LampAlarm";
    private static final boolean D = true;

    // Bluetooth related
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothMessageService mLampAlarmService = null;
    private ArrayAdapter<String> mMessageArrayAdapter;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 3;

    // Main GUI fragment pager
    FragmentPager mMainFragmentPager;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	if (D)
	    Log.e(TAG, "+++ ON CREATE +++");

	// Request action bar
	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

	// Set up the window layout
	setContentView(R.layout.activity_lamp_alarm_main);

	// Get local Bluetooth adapter
	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	// If the adapter is null, then Bluetooth is not supported
	if (mBluetoothAdapter == null) {
	    Toast.makeText(this, "Bluetooth is not available",
		    Toast.LENGTH_LONG).show();
	    finish();
	    return;
	}

	// Setup menu
	setContentView(R.layout.activity_lamp_alarm_main);

	// Fragment pager
	mMainFragmentPager = new FragmentPager(getSupportFragmentManager());

	// Set up the action bar.
	final ActionBar actionBar = getActionBar();
	actionBar.setHomeButtonEnabled(false);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	// Set up the ViewPager
	mViewPager = (ViewPager) findViewById(R.id.pager);
	mViewPager.setAdapter(mMainFragmentPager);
	mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		    @Override
		    public void onPageSelected(int position) {
			actionBar.setSelectedNavigationItem(position);
		    }
		});

	// Adds tabs for the fragments to the action bar
	for (int i = 0; i < mMainFragmentPager.getCount(); i++) {
	    actionBar.addTab(actionBar.newTab()
		    .setText(mMainFragmentPager.getPageTitle(i))
		    .setTabListener(this));
	}

	// Select the default tab
	actionBar.setSelectedNavigationItem(mMainFragmentPager.getDefaultTab());

    }

    /**
     * @return - Main fragment pager.
     */
    public FragmentPager getMainFragmentPager() {
        return mMainFragmentPager;
    }

    @Override
    public void onStart() {
	super.onStart();
	if (D)
	    Log.e(TAG, "++ ON START ++");

	// Turn on BT if not enabled
	if (!mBluetoothAdapter.isEnabled()) {
	    final Intent enableIntent = new Intent(
		    BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	} else {
	    if (mLampAlarmService == null) {
		setupLampAlarmSession();
	    }
	}
    }

    @Override
    public synchronized void onResume() {
	super.onResume();
	if (D)
	    Log.e(TAG, "+ ON RESUME +");

	// Performing this check in onResume() covers the case in which BT was
	// not enabled during onStart(), so we were paused to enable it...
	// onResume() will be called when ACTION_REQUEST_ENABLE activity
	// returns.
	if (mLampAlarmService != null) {
	    // Only if the state is STATE_NONE, do we know that we haven't
	    // started already
	    if (mLampAlarmService.getState() == BluetoothMessageService.STATE_NONE) {
		// Start the Bluetooth services
		mLampAlarmService.start();
	    }
	}
    }

    @Override
    public synchronized void onPause() {
	super.onPause();
	if (D)
	    Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
	super.onStop();
	if (D)
	    Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	// Stop the Bluetooth services
	if (mLampAlarmService != null)
	    mLampAlarmService.stop();
	if (D)
	    Log.e(TAG, "--- ON DESTROY ---");
    }

    private void setupLampAlarmSession() {
	if (D)
	    Log.d(TAG, "setupLampAlarmSession()");

	// Initialize the array adapter for the conversation thread
	setmMessageArrayAdapter(new ArrayAdapter<String>(this, R.layout.message));

	// Initialize the BluetoothMessageService
	final MessageHandler mHandler = new MessageHandler(this);
	mLampAlarmService = new BluetoothMessageService(this, mHandler);

    }

    /**
     * Sends a message.
     * 
     * @param message
     *            A string of text to send.
     * 
     * @return - True if message was sent, else false.
     */
    public boolean sendMessage(String message) {

	// Check that we're actually connected before trying anything
	if (mLampAlarmService.getState() != BluetoothMessageService.STATE_CONNECTED) {
	    Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
		    .show();
	    return false;
	}

	// Send the message
	if (message.length() > 0) {
	    final byte[] send = message.getBytes();
	    mLampAlarmService.write(send);
	    return true;
	}

	return false;
    }

    // Gets called after an activity returns
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (D)
	    Log.d(TAG, "onActivityResult " + resultCode);

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
		Toast.makeText(this, R.string.bt_not_enabled_leaving,
			Toast.LENGTH_SHORT).show();
		finish();
	    }
	}
    }

    private void connectDevice(Intent data) {
	// Get the device MAC address
	final String address = data.getExtras().getString(
		BluetoothConfigure.EXTRA_DEVICE_ADDRESS);
	// Get the BluetoothDevice object
	final BluetoothDevice device = mBluetoothAdapter
		.getRemoteDevice(address);
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

    public ArrayAdapter<String> getmConversationArrayAdapter() {
	return mMessageArrayAdapter;
    }

    public void setmMessageArrayAdapter(
	    ArrayAdapter<String> mConversationArrayAdapter) {
	this.mMessageArrayAdapter = mConversationArrayAdapter;
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
	mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
	    FragmentTransaction fragmentTransaction) {
    }

}
