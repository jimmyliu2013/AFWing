package com.lim.afwing.services;

import java.io.IOException;
import java.util.List;

import org.jsoup.HttpStatusException;

import com.lim.afwing.activitys.Constant;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;
import com.lim.afwing.dao.DataProviderManager;
import com.lim.afwing.utils.HtmlParser;


import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LoadWebDataService extends IntentService {

	public static final int MODE_LOADFORFIRSTTIME = 0;
	public static final int MODE_LOADNEWEST = 1;
	public static final int MODE_LOADMORE = 2;
	public static final String ACTION_LOAD_DATA = "com.lim.afwing.action.LOAD_DATA";
	public static final String KEY_RESULT = "result";
	private static final String TAG = "LoadWebDataService";
	public static final String KEY_MODE = "mode";
	public static final String KEY_ORDER_IN_LIST = "orderInList";
	private DataProviderManager mDataProviderManager;
	
	
	public LoadWebDataService() {
		super("LoadWebDataService");
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {

		//int orderInList = intent.getExtras().getInt("orderInList");
		String url = intent.getExtras().getString("url");
		String tabName = intent.getExtras().getString("tabName");
		mDataProviderManager = new DataProviderManager(getApplicationContext(), tabName);
		int mode = intent.getExtras().getInt("mode");
		
		//Log.d("lim", "tabname is : " + tabName + "\n" + "url is : " + url);
		
		
		if (tabName.equals(Contract.getTableName(0))) {
			try {
				
				List<TabListItemBean> pagerViewItemlist = HtmlParser.loadPagerViewAdapterData(url);
				List<TabListItemBean> listviewItemList = HtmlParser.loadIndexListViewAdapterData(url, Constant.HEADER_ITEM_COUNT);
				
				pagerViewItemlist.addAll(listviewItemList);
				
				if (pagerViewItemlist.size() > 0) {
					addListToContentProvier(pagerViewItemlist, mode);
					notify(tabName, Constant.SUCCESS, mode);
				}else {
					notify(tabName, Constant.END, mode);
				}
				
				
			}catch(HttpStatusException e){
				
				e.printStackTrace();
				notify(tabName, Constant.END, mode);
			
				
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
	else {
		
		//String html = null;
		try {
			List<TabListItemBean> list = HtmlParser.loadListData(url);
			//System.out.println(list.toString());
			//html = HttpUtils.getHtmlString(url);
			//List<ItemContent> list = HtmlParser.getItemList(html);
			
			if (list.size()>0) {
				addListToContentProvier(list, mode);
				notify(tabName, Constant.SUCCESS, mode);
			}else {
				notify(tabName, Constant.END, mode);
			}
		}catch(HttpStatusException e){
			
			e.printStackTrace();
			notify(tabName, Constant.END, mode);
		
			
		}
		catch (IOException e) {
			e.printStackTrace();
			notify(tabName, Constant.FAIL, mode);
		}
	}
		
	}

	private void addListToContentProvier(List<TabListItemBean> list, int mode) {
		 if (mode == Constant.MODE_LOADNEWEST) {
			 mDataProviderManager.deleteAll();
         }
		mDataProviderManager.bulkInsert(list);
		
		
	}

	private void notify(String orderInList, int result, int mode) {
		
		Intent localIntent =
	            new Intent(ACTION_LOAD_DATA)
		        .putExtra(KEY_ORDER_IN_LIST, orderInList)
	            .putExtra(KEY_RESULT, result)
	            .putExtra(KEY_MODE, mode);
	    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
	
	
	
	
	
	
}
