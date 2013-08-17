package org.cowboycoders.lampalarm;

import org.cowboycoders.lampalarm.menu.ColourSelectionFragment;
import org.cowboycoders.lampalarm.menu.MenuSectionFragment;
import org.cowboycoders.lampalarm.menu.TouchRotatableCubeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one
 * of the primary sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

	public AppSectionsPagerAdapter(FragmentManager fm) {
		super(fm);

	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
		case 0:
			final Fragment menuFragment = new MenuSectionFragment();
			final Bundle menuFragmentArgs = new Bundle();
			menuFragmentArgs.putInt(
					TouchRotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			menuFragment.setArguments(menuFragmentArgs);
			return menuFragment;
		case 1:
			final Fragment colourFragment = new ColourSelectionFragment();
			final Bundle colourFragmentArgs = new Bundle();
			colourFragmentArgs.putInt(
					TouchRotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			colourFragment.setArguments(colourFragmentArgs);
			return colourFragment;
		default:
			// The other sections of the app are dummy placeholders.
			final Fragment fragment = new TouchRotatableCubeFragment();
			final Bundle args = new Bundle();
			args.putInt(TouchRotatableCubeFragment.ARG_SECTION_NUMBER, i + 1);
			fragment.setArguments(args);
			return fragment;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Menu";
		case 1:
			return "Colour";
		case 2:
			return "Todo";
		default:
			return "Section " + (position + 1);
		}
	}
}