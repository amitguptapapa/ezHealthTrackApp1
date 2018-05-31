package com.ezhealthtrack.fragments;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ezhealthtrack.R;

public class ProfileFragment extends Fragment {
	private ProfileAdapter adapter;
	private ViewPager pager;
	public EducationFragment eduFragment = new EducationFragment();
	public ProfessionalFragment profFragment = new ProfessionalFragment();
	public PublicationFragment pubFragment = new PublicationFragment();
	public InsuranceFragment insFragment = new InsuranceFragment();

	public class ProfileAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Professional", "Education",
				"General / Insurance", "Publications" };

		public ProfileAdapter(final FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(final int position) {
			if (position == 1)
				return eduFragment;
			else if (position == 0)
				return profFragment;
			else if (position == 2)
				return insFragment;
			else if (position == 3)
				return pubFragment;
			else
				return new Fragment();
		}

		@Override
		public CharSequence getPageTitle(final int position) {
			return TITLES[position];
		}

	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		try {
			super.onActivityCreated(savedInstanceState);
			pager = (ViewPager) getActivity().findViewById(R.id.pager);
			adapter = new ProfileAdapter(getChildFragmentManager());
			pager.setAdapter(adapter);

			final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) getActivity()
					.findViewById(R.id.tabs);
			tabs.setViewPager(pager);
		} catch (Exception e) {

		}
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_profile, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private static final Field sChildFragmentManagerField;

	static {
		Field f = null;
		try {
			f = Fragment.class.getDeclaredField("mChildFragmentManager");
			f.setAccessible(true);
		} catch (NoSuchFieldException e) {
			Log.e("", "Error getting mChildFragmentManager field", e);
		}
		sChildFragmentManagerField = f;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (sChildFragmentManagerField != null) {
			try {
				sChildFragmentManagerField.set(this, null);
			} catch (Exception e) {
				Log.e("", "Error setting mChildFragmentManager field", e);
			}
		}
	}

}
