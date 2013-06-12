package org.cowboycoders.lampalarm;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		final View rootView = inflater.inflate(R.layout.menu_section,container, false);
		
		final Bundle args = getArguments();
		
		//FIXME: Pulled from main. Need to link an update of the frame buffer to the image.
		//button = (ImageView) findViewById(R.id.image);

		//FIXME:  Buttons need to call sendMessage in main.
		rootView.findViewById(R.id.button1).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						final String msg = ":a:";
						if (D) {
							Log.e(TAG, "+++ A pressed +++");
						}
						// sendMessage(msg);
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
						// sendMessage(msg);
						
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
						// sendMessage(msg);
				
					}
				});

 
		
		return rootView;
	}
}