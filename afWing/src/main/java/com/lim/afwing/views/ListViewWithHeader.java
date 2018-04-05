package com.lim.afwing.views;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lim.afwing.R;
import com.lim.afwing.adapter.TabListAdapter;
//import com.lim.afwing.fragments.IndexPageTabFragment.HeadViewPagerAdapter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewWithHeader extends LinearLayout {

	private PullToRefreshListView listView;
	private Context mContext;
	//private View headView;
	private ViewPager viewPager;
	private OnEventListener mOnEventListener;
	private int mFullScreenWidth;
	private View view;
	
	
	public PullToRefreshListView getListView() {
		return listView;
	}



	public void setListView(PullToRefreshListView listView) {
		this.listView = listView;
	}



	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}



	public ListViewWithHeader(Context context) {
		super(context);
		
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mFullScreenWidth = dm.widthPixels;
		initView(context, mFullScreenWidth);
		mContext = context;
    }

	
	
    public ListViewWithHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mFullScreenWidth = dm.widthPixels;
		initView(context, mFullScreenWidth);
		mContext = context;
	}


    public ViewPager getViewPager() {
		return viewPager;
	}

	private void initView(Context context, int fullScreenWidth) {
    	
        inflate(context, R.layout.listview_with_header, this);

        view = inflate(context, R.layout.header, null);
        
		viewPager = (ViewPager)view.findViewById(R.id.image_viewpager);
		
		
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				android.widget.AbsListView.LayoutParams.MATCH_PARENT, (int) (fullScreenWidth * 367/643 ));//fullScreenWidth, (int) (fullScreenWidth * 3/5 ));
		view.setLayoutParams(layoutParams);
		

		viewPager.setOnTouchListener(new OnTouchListener() {

			private int downX;
			private int downY;

			@Override
			public boolean onTouch(View v, MotionEvent evt) {

				switch (evt.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX = (int) evt.getX();
					downY = (int) evt.getY();
					v.getParent().requestDisallowInterceptTouchEvent(true);
					break;
				case MotionEvent.ACTION_MOVE:
					int moveX = (int) evt.getX();
					int moveY = (int) evt.getY();

					int diffX = (int) (downX - moveX);
					int diffY = (int) (downY - moveY);

					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (((ViewPager) v).getCurrentItem() == (((ViewPager) v)
								.getAdapter().getCount() - 1) && diffX > 0) {
							v.getParent().requestDisallowInterceptTouchEvent(
									false);
						} else {
							v.getParent().requestDisallowInterceptTouchEvent(
									true);
						}

					} else {
						v.getParent().requestDisallowInterceptTouchEvent(false);
					}

					break;
				case MotionEvent.ACTION_UP:
					if (PointF.length(evt.getX() - downX, evt.getY() - downY) < (float) 5.0) {
						
						mOnEventListener.onViewPagerClick(v);
					}
					break;
				}
				return false;
			}
		});

        
        
        
        
        listView = (PullToRefreshListView) findViewById(R.id.newest_listview);

		listView.setMode(Mode.BOTH);
		listView.setShowViewWhileRefreshing(true);
		listView.setDisableScrollingWhileRefreshing(false);
		listView.setSaveEnabled(true);
		listView.setBackgroundResource(R.drawable.listview_selector);
		listView.getRefreshableView().addHeaderView(view);
		listView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				mOnEventListener.onListViewPullDown();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				mOnEventListener.onListViewPullUp();
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mOnEventListener.onListItemClick(arg2);
			}

		});

    }

    public void setOnEventListener(OnEventListener listener) {
        mOnEventListener = listener;
	}
    
    

    public void setListViewAdapter(TabListAdapter adapter) {
		listView.getRefreshableView().setAdapter(adapter);
	}
    
    public void setViewPagerAdapter(PagerAdapter adapter) {
    	viewPager.setAdapter(adapter);
	}
    
	

	public interface OnEventListener{
		public void onViewPagerClick(View v);
		public void onViewPagerChange();
		public void onListItemClick(int position);
		public void onListViewPullDown();
		public void onListViewPullUp();
	}



	public void onRefreshComplete() {
		listView.onRefreshComplete();
	}



	public AbsListView getRefreshableView() {
		return listView.getRefreshableView();
	}
	
	
	
	
}
