package org.cowboycoders.lampalarm;


import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.menu_section,container, false);
		
		final Bundle args = getArguments();
	
		int initialColor = 0xff000000;
		
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), initialColor, new OnAmbilWarnaListener() {
	        @Override
	        public void onOk(AmbilWarnaDialog dialog, int color) {
	                // color is the color selected by the user.
	        }
	                
	        @Override
	        public void onCancel(AmbilWarnaDialog dialog) {
	                // cancel was selected by the user
	        }
		});
	
		dialog.show();
 
		return rootView;
	}
}