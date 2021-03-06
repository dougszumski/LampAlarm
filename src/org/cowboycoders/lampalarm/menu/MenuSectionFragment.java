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
 * Controls the lamp menu.
 * 
 * @author doug
 * 
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

		rootView.findViewById(R.id.button_left).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String msg = ":j:";
						if (D) {
							Log.e(TAG, "+++ J pressed +++");
						}
						lampAlarmMain.sendMessage(msg);
					}
				});

		rootView.findViewById(R.id.button_select).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String msg = ":k:";
						if (D) {
							Log.e(TAG, "+++ K pressed +++");
						}
						lampAlarmMain.sendMessage(msg);

					}
				});

		rootView.findViewById(R.id.button_right).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (D) {
							Log.e(TAG, "+++ L pressed +++");
						}
						final String msg = ":l:";
						lampAlarmMain.sendMessage(msg);

					}
				});

		return rootView;
	}
}