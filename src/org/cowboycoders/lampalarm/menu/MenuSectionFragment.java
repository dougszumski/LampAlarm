package org.cowboycoders.lampalarm.menu;

import org.cowboycoders.lampalarm.LampAlarmMain;
import org.cowboycoders.lampalarm.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class MenuSectionFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	// Debug
	private static final String TAG = "LampAlarm";
	private static final boolean D = true;

	// Image to be displayed
	ImageView button;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.menu_section,
				container, false);

		final Bundle args = getArguments();

		final LampAlarmMain lampAlarmMain = (LampAlarmMain) getActivity();

		rootView.findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String msg = ":a:";
						if (D) {
							Log.e(TAG, "+++ A pressed +++");
						}
						lampAlarmMain.sendMessage(msg);
					}
				});

		rootView.findViewById(R.id.button2).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String msg = ":s:";
						if (D) {
							Log.e(TAG, "+++ S pressed +++");
						}
						lampAlarmMain.sendMessage(msg);

					}
				});

		rootView.findViewById(R.id.button3).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (D) {
							Log.e(TAG, "+++ D pressed +++");
						}
						final String msg = ":d:";
						lampAlarmMain.sendMessage(msg);

					}
				});

		return rootView;
	}
}