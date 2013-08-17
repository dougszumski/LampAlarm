package org.cowboycoders.lampalarm;

import java.util.List;

import org.cowboycoders.lampalarm.backend.BitmapGenerator;
import org.cowboycoders.lampalarm.backend.BluetoothConfigure;
import org.cowboycoders.lampalarm.backend.BluetoothMessageService;
import org.cowboycoders.lampalarm.backend.MessageHandler;

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

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;

	// Intent request code
	private static final int REQUEST_CONNECT_DEVICE = 1;
	private static final int REQUEST_ENABLE_BT = 3;

	private ArrayAdapter<String> mConversationArrayAdapter;

	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;

	// Member object for the chat services
	private BluetoothMessageService mLampAlarmService = null;

	private static final int MINIMUM_TIME_BETWEEN_MESSAGES_MS = 10;
	private long timeStamp = System.currentTimeMillis();

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D) {
			Log.e(TAG, "+++ ON CREATE +++");
		}

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

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D) {
			Log.e(TAG, "++ ON START ++");
		}

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

	private void setupLampAlarmSession() {
		Log.d(TAG, "setupLampAlarmSession()");

		// Initialize the array adapter for the conversation thread
		setmConversationArrayAdapter(new ArrayAdapter<String>(this,
				R.layout.message));

		// Initialize the BluetoothMessageService to perform bluetooth
		// connections
		final MessageHandler mHandler = new MessageHandler(this);
		mLampAlarmService = new BluetoothMessageService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");

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

		// Update the timeStamp
		final long timeNow = System.currentTimeMillis();
		final long timeDelta = timeNow - timeStamp;
		timeStamp = timeNow;
		if (timeDelta < MINIMUM_TIME_BETWEEN_MESSAGES_MS) {
			return false;
		}

		// Check that we're actually connected before trying anything
		if (mLampAlarmService.getState() != BluetoothMessageService.STATE_CONNECTED) {

			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return false;
		}

		// Send the message
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			final byte[] send = message.getBytes();
			mLampAlarmService.write(send);
			return true;
		}

		return false;
	}

	// Gets called after an activity returns
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D) {
			Log.d(TAG, "onActivityResult " + resultCode);
		}
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

	/**
	 * Updates the image on the screen.
	 * 
	 * @param frame
	 *            - Frame to update image with.
	 */
	public void updateImage(List<Integer> frame) {

		final BitmapGenerator bitmapGenerator = new BitmapGenerator(128, 64);
		final Bitmap frameBuffer = bitmapGenerator.byteArrayToBitmap(frame);
		final ImageView button = (ImageView) findViewById(R.id.image);
		button.setImageBitmap(frameBuffer);
	}

	public ArrayAdapter<String> getmConversationArrayAdapter() {
		return mConversationArrayAdapter;
	}

	public void setmConversationArrayAdapter(
			ArrayAdapter<String> mConversationArrayAdapter) {
		this.mConversationArrayAdapter = mConversationArrayAdapter;
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
