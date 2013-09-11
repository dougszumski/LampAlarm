package org.cowboycoders.lampalarm.fragments;

import java.util.List;

import org.cowboycoders.lampalarm.LampAlarmMain;
import org.cowboycoders.lampalarm.R;
import org.cowboycoders.lampalarm.backend.BitmapGenerator;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Remote control for the lamp. Sends commands and builds a virtual screen.
 * 
 * @author Doug Szumski
 * 
 */
public class RemoteControlFragment extends Fragment {

    public static final String ARG_SECTION_NUMBER = "section_number";

    // Lamp commands for buttons
    public static final String LEFT_COMMAND = ":j:";
    public static final String SELECT_COMMAND = ":k:";
    public static final String RIGHT_COMMAND = ":l:";

    // Debug
    private static final String TAG = "LampAlarm";
    private static final boolean D = true;

    // Virtual screen to display
    public static final int X_PIXELS = 128;
    public static final int Y_PIXELS = 64;
    ImageView virtualScreen;

    View mRootView;
    LampAlarmMain mLampAlarmMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	mRootView = inflater.inflate(R.layout.menu_section, container, false);
	mLampAlarmMain = (LampAlarmMain) getActivity();
	//final Bundle args = getArguments();

	mRootView.findViewById(R.id.button_left).setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			sendMessage(LEFT_COMMAND);
		    }
		});

	mRootView.findViewById(R.id.button_select).setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			sendMessage(SELECT_COMMAND);
		    }
		});

	mRootView.findViewById(R.id.button_right).setOnClickListener(
		new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			sendMessage(RIGHT_COMMAND);
		    }
		});

	return mRootView;
    }
    
    /**
     * Sends a Bluetooth message.
     * 
     * @param msg - Message to send.
     */
    private void sendMessage(String msg) {
	if (D) {
	    Log.e(TAG, "Sent message: " + msg);
	}
	mLampAlarmMain.sendMessage(msg);
    }

    /**
     * Updates the virtual screen.
     * 
     * @param frame
     *            - Frame to update image with.
     */
    public void updateImage(List<Integer> frame) {

	final BitmapGenerator bitmapGenerator = new BitmapGenerator(X_PIXELS, Y_PIXELS);
	final Bitmap frameBuffer = bitmapGenerator.byteArrayToBitmap(frame);
	final ImageView button = (ImageView) mRootView.findViewById(R.id.image);
	button.setImageBitmap(frameBuffer);
    }
}
