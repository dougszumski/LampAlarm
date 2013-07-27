package org.cowboycoders.lampalarm.menu;

import org.cowboycoders.lampalarm.LampAlarmMain;
import org.cowboycoders.lampalarm.R;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class ColourSelectionFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	// Debug
	private static final String TAG = "LampAlarm";
	private static final boolean D = true;

	// Image to be displayed
	ImageView button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.color_chooser_section,
				container, false);

		final Bundle args = getArguments();
		
		final LampAlarmMain lampAlarmMain = (LampAlarmMain) getActivity();

		final int initialColor = 0x000000;

		final AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(),
				initialColor, new OnAmbilWarnaListener() {
			
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						// cancel was selected by the user
					}

					@Override
					public void onUpdate(AmbilWarnaDialog dialog, int color) {
						// Get the first three bytes of color and put in a string
						String msg = ":p" + Integer.toHexString(color).substring(2).toUpperCase() +":";
						if (D) {
							Log.e(TAG, "Sending colour: " + msg);
						}
						lampAlarmMain.sendMessage(msg);
						
					}
				});

		final Button colourButton = (Button) rootView
				.findViewById(R.id.button1);
		colourButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

		return rootView;
	}
}