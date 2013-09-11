package org.cowboycoders.lampalarm.backend;

import java.util.Locale;

import org.cowboycoders.lampalarm.LampAlarmMain;

import android.util.Log;

import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.colorpicker.SVBar;

/**
 * Updates the lamp colour and the left side of the colour picker if the message
 * is sent.
 * 
 * @author doug
 * 
 */
public class ColourChangedListener implements OnColorChangedListener {

	// Debug
	private static final String TAG = "ColourListener";
	private static final boolean D = true;

	private final LampAlarmMain lampAlarmMain;
	private final ColorPicker picker;
	private final SVBar saturationBrightnessBar;

	public ColourChangedListener(LampAlarmMain lampAlarmMain,
			ColorPicker picker, SVBar saturationBrightnessBar) {
		this.lampAlarmMain = lampAlarmMain;
		this.picker = picker;
		this.saturationBrightnessBar = saturationBrightnessBar;

	}

	@Override
	public void onColorChanged(int color) {

		color = saturationBrightnessBar.getColor();

		// Get the first three bytes of colour and put in a
		// string
		final String msg = ":p"
				+ Integer.toHexString(color).substring(2)
						.toUpperCase(Locale.getDefault()) + ":";
		if (D) {
			Log.e(TAG, "Sending colour: " + msg);
		}

		if (lampAlarmMain.sendMessage(msg)) {
			// Update the set colour
			picker.setOldCenterColor(color);
		}

	}

}
