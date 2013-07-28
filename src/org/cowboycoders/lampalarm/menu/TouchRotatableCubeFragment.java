/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cowboycoders.lampalarm.menu;

import org.cowboycoders.lampalarm.R;
import org.cowboycoders.lampalarm.touchableCube.TouchSurfaceView;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Rotatable cube fragment based on API example.
 * 
 */
public class TouchRotatableCubeFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	private GLSurfaceView mGLSurfaceView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View rootView = inflater.inflate(R.layout.fragment_section_dummy,
				container, false);
		final Bundle args = getArguments();
		((TextView) rootView.findViewById(android.R.id.text1))
				.setText(getString(R.string.dummy_section_text,
						args.getInt(ARG_SECTION_NUMBER)));

		// Create our Preview view and set it as the content of our
		// Activity
		mGLSurfaceView = new TouchSurfaceView(getActivity());
		mGLSurfaceView.requestFocus();
		mGLSurfaceView.setFocusableInTouchMode(true);

		// Add a listener for the target colour

		/*
		 * ImageView imageView = ((ImageView)v); Bitmap bitmap =
		 * ((BitmapDrawable)imageView.getDrawable()).getBitmap(); int pixel =
		 * bitmap.getPixel(x,y);
		 * 
		 * Now you can get each channel with:
		 * 
		 * int redValue = Color.red(pixel); int blueValue = Color.blue(pixel);
		 * int greenValue = Color.green(pixel);
		 */

		return mGLSurfaceView;
	}
}
