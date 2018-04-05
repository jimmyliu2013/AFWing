package com.lim.afwing.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lim.afwing.R;
import com.lim.afwing.activitys.Constant;
import com.lim.afwing.activitys.WebViewActivity;
import com.lim.afwing.adapter.HeaderViewPagerAdapter;
import com.lim.afwing.adapter.IndexListAdapter;
import com.lim.afwing.adapter.TabListAdapter;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;
import com.lim.afwing.dao.DataProviderManager;
import com.lim.afwing.services.LoadWebDataService;
import com.lim.afwing.views.ListViewWithHeader;

public class IndexPageTabFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		ListViewWithHeader.OnEventListener {

	public final int END = 2;
	private String mCatalogUrl;
	private int currentIndex = 1;

	private Context mContext;
	public String currentUrl;
	private PullToRefreshListView listView;
	private View rootView;
	public boolean isRefreshing;
	private int mOrderInList;
	private DataProviderManager mDataProviderManager;
	public boolean mIsRefreshing;
	private ResponseReceiver responseReceiver;
	private TabListAdapter tabListAdapter;
	private HeaderViewPagerAdapter headerViewPagerAdapter;
	private ListViewWithHeader listViewWithHeader;
	private Parcelable onSaveInstanceState;

	public static final IndexPageTabFragment newInstance(int orderInList,
			String catalogName, String catalogUrl) {
		IndexPageTabFragment f = new IndexPageTabFragment();
		Bundle bdl = new Bundle();
		bdl.putInt("orderInList", orderInList);
		bdl.putString("catalogName", catalogName);
		bdl.putString("catalogUrl", catalogUrl);
		f.setArguments(bdl);
			
		return f;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		mOrderInList = getArguments().getInt("orderInList");
		mCatalogUrl = getArguments().getString("catalogUrl");
        
		mDataProviderManager = new DataProviderManager(mContext, Contract.getTableName(mOrderInList));
		responseReceiver = new ResponseReceiver();

		
		IntentFilter responseIntentFilter = new IntentFilter(
				LoadWebDataService.ACTION_LOAD_DATA);
		
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				responseReceiver, responseIntentFilter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_index, null);

			listViewWithHeader = (ListViewWithHeader) rootView
					.findViewById(R.id.list_view_with_header);
			tabListAdapter = new IndexListAdapter(mContext, null);
			listViewWithHeader.setListViewAdapter(tabListAdapter);
			headerViewPagerAdapter = new HeaderViewPagerAdapter(mContext, null);
			headerViewPagerAdapter.notifyDataSetChanged();
			listViewWithHeader.setViewPagerAdapter(headerViewPagerAdapter);
			listViewWithHeader.setOnEventListener(this);

		}


		
		return rootView;

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getLoaderManager().initLoader(0, null, this);
		if (onSaveInstanceState != null) {
			
			Log.d("lim", "saved state not null, restore");
			listViewWithHeader.getRefreshableView().onRestoreInstanceState(onSaveInstanceState);
		}
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();

		LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
				responseReceiver);

		if (rootView != null) {
			if (rootView.getParent() != null) {
				((ViewGroup) rootView.getParent()).removeView(rootView);
			}
		}
		if (listView != null && listView.isRefreshing()) {
			listView.onRefreshComplete();
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		changeMode();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		
		listViewWithHeader.onRefreshComplete();
		onSaveInstanceState = listViewWithHeader.getRefreshableView().onSaveInstanceState();
	}

	private class ResponseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String orderInList = intent.getExtras().getString(
					LoadWebDataService.KEY_ORDER_IN_LIST);
			if ((Contract.getTableName(mOrderInList)).equals(orderInList)) {
				int statu = intent.getExtras().getInt(
						LoadWebDataService.KEY_RESULT);
				int mode = intent.getExtras().getInt(
						LoadWebDataService.KEY_MODE);

				switch (statu) {
				case Constant.SUCCESS:
					if (mode == Constant.MODE_LOADMORE) {
						currentIndex += 1;
					} else {
						currentIndex = 1;
					}

					break;
				case Constant.END:
					Toast.makeText(getActivity(), getResources().getString(R.string.load_end_toast_text), 0).show();
					break;
				case Constant.FAIL:
					Toast.makeText(getActivity(), getResources().getString(R.string.search_fail_toast_text), 0).show();
					break;

				default:
					break;
				}

				mIsRefreshing = false;
				listViewWithHeader.onRefreshComplete();
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return mDataProviderManager.getCursorLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		headerViewPagerAdapter.swapCursor(arg1);
		headerViewPagerAdapter.notifyDataSetChanged();
		tabListAdapter.swapCursor(arg1);

		if (arg1.getCount() == 0) {
			listViewWithHeader.getListView().setRefreshing();
			loadData(Constant.MODE_LOADNEWEST);
		}
		
		if (onSaveInstanceState != null) {
			listViewWithHeader.getRefreshableView().onRestoreInstanceState(onSaveInstanceState);
			onSaveInstanceState = null;
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		headerViewPagerAdapter.swapCursor(null);
		headerViewPagerAdapter.notifyDataSetChanged();
		tabListAdapter.swapCursor(null);
	}

	@Override
	public void onViewPagerClick(View v) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("linkUrl",
				((HeaderViewPagerAdapter) (((ViewPager) v)).getAdapter())
						.getItem(((ViewPager) v).getCurrentItem()).linkUrl);
		startActivity(intent);
	}

	@Override
	public void onViewPagerChange() {

	}

	@Override
	public void onListItemClick(int position) {
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra(
				"linkUrl",
				((TabListItemBean) tabListAdapter.getItem(position - 2)).linkUrl);
		startActivity(intent);
	}

	@Override
	public void onListViewPullDown() {
		if (!mIsRefreshing) {
			loadData(Constant.MODE_LOADNEWEST);
		}
	}

	@Override
	public void onListViewPullUp() {
		if (!mIsRefreshing) {
			loadData(Constant.MODE_LOADMORE);

		}
	}

	private void loadData(int mode) {
		String url = mCatalogUrl;
		mIsRefreshing = true;
		if (mode == Constant.MODE_LOADMORE) {
			if (currentIndex == 1) {
				currentIndex = 2;
			}
			url = mCatalogUrl + "list/index_" + (currentIndex + 1) + ".html";
		}

		Intent intent = new Intent(mContext, LoadWebDataService.class);
		intent.putExtra("url", url);
		intent.putExtra("mode", mode);
		intent.putExtra("tabName", Contract.getTableName(mOrderInList));
		getActivity().startService(intent);
	}
	
	public void changeMode(){
		
		listViewWithHeader.getViewPager().getAdapter().notifyDataSetChanged();
		listViewWithHeader.getRefreshableView().invalidateViews();
		if (MyApplication.isNightMode()) {
			rootView.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
		}else {
			rootView.setBackgroundColor(getResources().getColor(R.color.day_mode_background));
		}
	}
}
