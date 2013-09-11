package org.cowboycoders.lampalarm.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the primary sections of the app.
 */
public class FragmentPager extends FragmentPagerAdapter {
	
	private static final int DEFAULT_TAB = 1;
	
	private static final String TITLE_REMOTE_FRAGMENT = "Remote";
	private static final String TITLE_COLOUR_FRAGMENT = "Colour";
	private static final String TITLE_TODO_FRAGMENT = "Todo";
	
	private RemoteControlFragment mRemoteControlFragment;

	public FragmentPager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			mRemoteControlFragment = new RemoteControlFragment();
			final Bundle menuFragmentArgs = new Bundle();
			menuFragmentArgs.putInt(
					RotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			mRemoteControlFragment.setArguments(menuFragmentArgs);
			return mRemoteControlFragment;
		case 1:
			final Fragment colourFragment = new ColourSelectionFragment();
			final Bundle colourFragmentArgs = new Bundle();
			colourFragmentArgs.putInt(
					RotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			colourFragment.setArguments(colourFragmentArgs);
			return colourFragment;
		default:
			final Fragment fragment = new RotatableCubeFragment();
			final Bundle args = new Bundle();
			args.putInt(RotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			fragment.setArguments(args);
			return fragment;
		}
	}
	
	/**
	 * @return - Default tab.
	 */
	public int getDefaultTab() {
		return DEFAULT_TAB;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return TITLE_REMOTE_FRAGMENT;
		case 1:
			return TITLE_COLOUR_FRAGMENT;
		case 2:
			return TITLE_TODO_FRAGMENT;
		default:
			return "Section " + (position + 1);
		}
	}
	
	public RemoteControlFragment getmRemoteControlFragment() {
	    return mRemoteControlFragment;
	}
}