package org.cowboycoders.lampalarm.menu;

import org.cowboycoders.lampalarm.LampAlarmMain;
import org.cowboycoders.lampalarm.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.SVBar;

/**
 * Dialogue to choose the lamp colour
 * 
 * @author doug
 *
 */
public class ColourSelectionFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	// Debug
	private static final String TAG = "LampAlarm";
	private static final boolean D = true;
	
	private static final int DEFAULT_COLOUR = 0xFFF566;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.color_chooser_section,
				container, false);

		final LampAlarmMain lampAlarmMain = (LampAlarmMain) getActivity();

		// Make the colour picker and control bar
		final ColorPicker picker = (ColorPicker) rootView
				.findViewById(R.id.picker);
		picker.setColor(DEFAULT_COLOUR);
		final SVBar saturationBrightnessBar = (SVBar) rootView
				.findViewById(R.id.svbar);
		picker.addSVBar(saturationBrightnessBar);
		
		// Update lamp colour on selection
		picker.setOnColorChangedListener(new ColourChangedListener(
				lampAlarmMain, picker, saturationBrightnessBar));

		return rootView;
	}
}