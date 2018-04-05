package com.lim.afwing.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lim.afwing.R;
import com.lim.afwing.activitys.Constant;
import com.lim.afwing.activitys.WebViewActivity;
import com.lim.afwing.adapter.TabListAdapter;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;
import com.lim.afwing.dao.DataProviderManager;
import com.lim.afwing.services.LoadWebDataService;

public class TabFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	protected static final String TAG = "TabFragment";
	private int mOrderInList;
	private String mCatalogName;
	private String mCatalogUrl;
	private String currentUrl;
	private PullToRefreshListView listView;
	public String getmCatalogName() {
		return mCatalogName;
	}

	public void setmCatalogName(String mCatalogName) {
		this.mCatalogName = mCatalogName;
	}

	public String getmCatalogUrl() {
		return mCatalogUrl;
	}

	public void setmCatalogUrl(String mCatalogUrl) {
		this.mCatalogUrl = mCatalogUrl;
	}

	public List<HashMap<String, String>> getmSubPageList() {
		return mSubPageList;
	}

	public void setmSubPageList(List<HashMap<String, String>> mSubPageList) {
		this.mSubPageList = mSubPageList;
	}

	private List<HashMap<String, String>> mSubPageList;

	private int currentIndex = 1;

	private Context mContext;
	public String tempCurrentUrl;
	private View rootView;
	public boolean isRefreshing;
	private DataProviderManager mDataProviderManager;
	private TabListAdapter mTabListAdapter;
	private ResponseReceiver responseReceiver;
	private boolean mIsRefreshing = false;
	private Parcelable onSaveInstanceState;

	public static final TabFragment newInstance(int orderInList,
			String catalogName, String catalogUrl,
			ArrayList<HashMap<String, String>> subPageList) {
		TabFragment f = new TabFragment();
		Bundle bdl = new Bundle();
		bdl.putSerializable("subPageList", subPageList);
		bdl.putInt("orderInList", orderInList);
		bdl.putString("catalogName", catalogName);
		bdl.putString("catalogUrl", catalogUrl);
		f.setArguments(bdl);
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = getActivity();
		mOrderInList = getArguments().getInt("orderInList");
		mCatalogName = getArguments().getString("catalogName");
		mCatalogUrl = getArguments().getString("catalogUrl");
		mSubPageList = (List<HashMap<String, String>>) getArguments()
				.getSerializable("subPageList");

		currentUrl = mCatalogUrl;

		
		if (mDataProviderManager == null) {
			mDataProviderManager = new DataProviderManager(mContext, Contract.getTableName(mOrderInList));
		}
		

		IntentFilter responseIntentFilter = new IntentFilter(
				LoadWebDataService.ACTION_LOAD_DATA);
		responseReceiver = new ResponseReceiver();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				responseReceiver, responseIntentFilter);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_layout, container, false);
			initListView(rootView);
		}


		return rootView;

	}

	
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getLoaderManager().initLoader(0, null, this);

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
		// TODO Auto-generated method stub
		super.onResume();
		changeMode();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		listView.onRefreshComplete();
		onSaveInstanceState = listView.getRefreshableView().onSaveInstanceState();
	}
	

	@SuppressWarnings("unchecked")
	private void initListView(View view) {
		listView = (PullToRefreshListView) view.findViewById(R.id.tab_listview);

		listView.setMode(Mode.BOTH);
		listView.setShowViewWhileRefreshing(true);

		mTabListAdapter = new TabListAdapter(mContext, null);

		listView.setAdapter(mTabListAdapter);

		listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				if (!mIsRefreshing) {
					loadData(Constant.MODE_LOADNEWEST);
				}

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				if (!mIsRefreshing) {
					loadData(Constant.MODE_LOADMORE);

				}
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(mContext, WebViewActivity.class);
				intent.putExtra(
						"linkUrl",
						((TabListItemBean) mTabListAdapter.getItem(arg2 - 1)).linkUrl);
				startActivity(intent);

			}

		});
	}

	public void changePage(String subUrl) {
		currentUrl = mCatalogUrl + subUrl;// mSubPageList.get(index).get("subPageURL");
		loadData(Constant.MODE_LOADNEWEST);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return mDataProviderManager.getCursorLoader();
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		mTabListAdapter.swapCursor(arg1);
		if (arg1.getCount() == 0) {
			listView.setRefreshing();
			loadData(Constant.MODE_LOADNEWEST);
		}
		if (onSaveInstanceState != null) {
			listView.getRefreshableView().onRestoreInstanceState(onSaveInstanceState);
			onSaveInstanceState = null;
		}
		
		
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mTabListAdapter.swapCursor(null);
	}

	private void loadData(int mode) {
		mIsRefreshing = true;
		String url = currentUrl;
		if (mode == Constant.MODE_LOADMORE) {
			url = currentUrl + "index_" + (currentIndex + 1) + ".html";
		}

		Intent intent = new Intent(mContext, LoadWebDataService.class);
		// intent.putExtra("orderInList", mOrderInList);
		intent.putExtra("url", url);
		intent.putExtra("mode", mode);
		intent.putExtra("tabName", Contract.getTableName(mOrderInList));
		getActivity().startService(intent);
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
				listView.onRefreshComplete();
			}
		}
	}
	
	public void changeMode(){
		
		//mTabListAdapter.changeMode();
		listView.getRefreshableView().invalidateViews();
		if (MyApplication.isNightMode()) {
			rootView.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
		}else {
			rootView.setBackgroundColor(getResources().getColor(R.color.day_mode_background));
		}
		
	}

}
