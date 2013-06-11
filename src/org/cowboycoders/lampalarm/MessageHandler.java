package org.cowboycoders.lampalarm;

import java.util.List;

import android.app.ActionBar;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MessageHandler extends Handler {

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Debug
	private static final String TAG = "Message Handler";
	private static final boolean D = true;

	// Name of the connected device
	private String mConnectedDeviceName = null;

	private final LampAlarmMain lampAlarmMain;

	public MessageHandler(LampAlarmMain lampAlarmMain) {
		this.lampAlarmMain = lampAlarmMain;

	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_STATE_CHANGE:
			if (D) {
				Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
			}
			switch (msg.arg1) {
			case BluetoothMessageService.STATE_CONNECTED:
				setStatus(lampAlarmMain.getString(R.string.title_connected_to,
						mConnectedDeviceName));
				lampAlarmMain.getmConversationArrayAdapter().clear();
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
			final byte[] writeBuf = (byte[]) msg.obj;
			// Construct a string from the buffer
			final String writeMessage = new String(writeBuf);
			lampAlarmMain.getmConversationArrayAdapter().add(
					"Me:  " + writeMessage);
			break;
		case MESSAGE_READ:
			final List<Integer> frame = (List<Integer>) msg.obj;
			if (D) {
				Log.i(TAG, "Message arrived: " + frame.size());
			}
			lampAlarmMain.updateImage(frame);
			break;
		case MESSAGE_DEVICE_NAME:
			// save the connected device's name
			mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
			Toast.makeText(lampAlarmMain.getApplicationContext(),
					"Connected to " + mConnectedDeviceName, Toast.LENGTH_SHORT)
					.show();
			break;
		case MESSAGE_TOAST:
			Toast.makeText(lampAlarmMain.getApplicationContext(),
					msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
			break;
		}

	}

	private final void setStatus(int resId) {
		final ActionBar actionBar = lampAlarmMain.getActionBar();
		actionBar.setSubtitle(resId);
	}

	private final void setStatus(CharSequence subTitle) {
		final ActionBar actionBar = lampAlarmMain.getActionBar();
		actionBar.setSubtitle(subTitle);
	}

}
