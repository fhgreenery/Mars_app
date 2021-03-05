package com.sjzc.fh.pedometer.adapter;

import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sjzc.fh.pedometer.ui.fragment.LauncherBaseFragment;

/**
 * Viewpager
 * @author fh
 *
 */
public class BaseFragmentAdapter extends FragmentStatePagerAdapter {
	private List<LauncherBaseFragment>list;
	public BaseFragmentAdapter(FragmentManager fm, List<LauncherBaseFragment> list) {
		super(fm);
		this.list = list;
	}

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}
}
