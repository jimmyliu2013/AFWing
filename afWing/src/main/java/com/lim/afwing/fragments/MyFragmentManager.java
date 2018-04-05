package com.lim.afwing.fragments;

import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;

import com.lim.afwing.beans.PageInfoBean;




public class MyFragmentManager {

	private static MyFragmentManager mFragmentManager = null;
	private HashMap<Integer, Fragment> mFragmentMap = new HashMap<>();
	private List<PageInfoBean> mPageInfoList = null;

	private MyFragmentManager(List<PageInfoBean> pageInfoList) {
		mPageInfoList = pageInfoList;
	}
	
	public static MyFragmentManager getInstance(List<PageInfoBean> pageInfoList){
		if (mFragmentManager == null) {
			mFragmentManager = new MyFragmentManager(pageInfoList);
		}
		return mFragmentManager;
	}
	
	public Fragment getFragment(int position){
		
		if (mFragmentMap.get(position) != null) {
			return mFragmentMap.get(position);
		}else{
			
			Fragment fragment = null;
			if (position == 0) {
				fragment = IndexPageTabFragment.newInstance(mPageInfoList.get(position).getOrderInList(), mPageInfoList.get(0).getPageName(), mPageInfoList.get(0).getPageURL());
			}else{
			
			    fragment = TabFragment.newInstance(mPageInfoList.get(position).getOrderInList(), mPageInfoList.get(position).getPageName(), mPageInfoList.get(position).getPageURL(), mPageInfoList.get(position).getSubPageList());
			
			}//Fragment fragment = null;
			mFragmentMap.put(position, fragment);
			return fragment;
		}
		
	}
	
	public void clear() {
		mFragmentMap.clear();

	}
	
}