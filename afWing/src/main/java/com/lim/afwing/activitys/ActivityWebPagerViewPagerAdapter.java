package com.lim.afwing.activitys;


import com.lim.afwing.fragments.WebFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class ActivityWebPagerViewPagerAdapter extends FragmentStatePagerAdapter {

	private int pageNumber;
	private String url;
		
	
	public ActivityWebPagerViewPagerAdapter(FragmentManager fm, int pageNumber, String url) {
		super(fm);
		this.pageNumber = pageNumber;
		this.url = url;
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageNumber;
	}

/*	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}*/

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return WebFragment.newInstance(url, arg0+1, pageNumber);
	}

}
