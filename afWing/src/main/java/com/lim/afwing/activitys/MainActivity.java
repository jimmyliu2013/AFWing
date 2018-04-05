package com.lim.afwing.activitys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lim.afwing.R;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.PageInfoBean;
import com.lim.afwing.dao.Contract;
import com.lim.afwing.dao.DataProvider;
import com.lim.afwing.fragments.MyFragmentManager;
import com.lim.afwing.fragments.TabFragment;
import com.lim.afwing.utils.CommonUtils;
import com.lim.afwing.utils.DimensionUtils;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends ActionBarActivity {

	private SlidingMenu slidingMenu;
	private MyPagerAdapter myPagerAdapter;
	private ViewPager viewPager;
	private TabPageIndicator indicator;
	private int currentPage = 0;
	private ImageButton leftButton;
	private List<PageInfoBean> pageInfoBeanList;
	private List<List<HashMap<String, String>>> arrayOfSlidingmenuItemList ;
	private SimpleAdapter slidingmenuAdapter;
	private ListView slidingmenuList;
	private List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
	private long exitTime = 0;
	private MyFragmentManager mMyFragmentManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String pageInfo = intent.getExtras().getString("pageInfoBeanList");
			try {
				pageInfoBeanList = initPageInfoBeanList(pageInfo);
			} catch (JSONException e) {
				e.printStackTrace();
				throw new RuntimeException("json parse page info list error!");
			}
		arrayOfSlidingmenuItemList = new ArrayList<List<HashMap<String,String>>>();
		for (int i = 0; i < pageInfoBeanList.size(); i++) {
			arrayOfSlidingmenuItemList.add(pageInfoBeanList.get(i).getSubPageList());
			
			HashMap<String, String> map = new HashMap<String, String>();
			
			map.put("subPageName", getResources().getString(R.string.sub_page_list_all));
			map.put("subPageURL", "");
			arrayOfSlidingmenuItemList.get(i).add(0, map);
		}
		
		mMyFragmentManager = MyFragmentManager.getInstance(pageInfoBeanList);
		
		
		//Log.d("lim", "page info list size : " + pageInfoBeanList.size());
		checkDataBase(pageInfoBeanList);
		
		initGlide();
		
		
		
		initSlidingMenu();

		initActionbar();

		initViewPager();// must before init indicator
		
		initViewPagerIndicator();
		
		initSlidingMenu();
		
		

	}
	
	
	
	
	@Override
	protected void onResume() {
		
        CommonUtils.setActivityBackground(this, MyApplication.isNightMode());
		super.onResume();
	}




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMyFragmentManager.clear();
	}




	private void initGlide() {
		if (!Glide.isSetup()) {
			GlideBuilder gb = new GlideBuilder(this);
			DiskCache dlw = null;
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// sd card 可用
				Log.i("AFWing", "create image cache dir on SD card");
				dlw = DiskLruCacheWrapper.get(new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/" + Constant.IMAGE_CACHE_DIR_NAME + "/"), 50 * 1024 * 1024);
			} else {
				// 当前不可用
				Log.i("AFWing", "create image cache dir on cache");
				dlw = DiskLruCacheWrapper.get(new File(getCacheDir().getAbsolutePath()
						+ "/" + Constant.IMAGE_CACHE_DIR_NAME + "/"), 30 * 1024 * 1024);
			
			}
			gb.setDiskCache(dlw);
			gb.setMemoryCache(new LruResourceCache(5000000));
			Glide.setup(gb);
		}
	}
	
	
	
	private void checkDataBase(List list) {
		// TODO Auto-generated method stub
		//if (list.size() != Contract.COUNT_OF_TABLE) {
			//Log.i("AFWing", "delete old database");
			
			//DataProvider.deleteAllDatabases();
			
			/*try {
				deleteDatabase(Contract.DB_NAME);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("AFWing", "delete old database failed");
			}*/
			
		}
	








	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu, menu);
		menu.findItem(R.id.action_bar_search);
		return super.onCreateOptionsMenu(menu);
	}








	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_search:
				showPopupWindow(getSupportActionBar().getCustomView());
			return true;

	case R.id.action_bar_settings:
		Intent intent1 = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(intent1);
		
		return true;
		
		
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private void initActionbar() {
		ActionBar actionBar = getSupportActionBar();
		
		 View customView = getLayoutInflater().inflate(R.layout.actionbar_custom_layout, null);
		 actionBar.setDisplayShowHomeEnabled(false);
		    actionBar.setDisplayShowCustomEnabled(true);
		    actionBar.setDisplayShowTitleEnabled(false);
		 actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		    actionBar.setCustomView(customView);
		    Toolbar parent =(Toolbar) customView.getParent();
		    parent.setContentInsetsAbsolute(0,0);
		leftButton = (ImageButton) findViewById(R.id.actionbar_leftbutton);
		
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
				
		});
		
		
	}
	
	

	private void initViewPagerIndicator() {
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(final int arg0) {
				currentPage = arg0;
				setSlidingMenuItemAfterPageSelected(arg0);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		indicator.setViewPager(viewPager);
		indicator.setCurrentItem(0);

	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());//(getSupportFragmentManager());
		viewPager.setAdapter(myPagerAdapter);
		
	}

	
	
	
	public class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return mMyFragmentManager.getFragment(arg0);

		}

		@Override
		public int getCount() {
			return pageInfoBeanList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return pageInfoBeanList.get(position).getPageName();
		}
	}
	
	private void initSlidingMenu() {
		slidingMenu = new SlidingMenu(this);

		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		slidingMenu.setBehindWidth((int) getResources().getDimension(
				R.dimen.sliding_menu_width));
		slidingMenu.setBackgroundColor(Color.BLACK);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setMenu(R.layout.slidingmenu_layout);
		slidingmenuList = (ListView) findViewById(R.id.slidingmenu_listview);
	}

	
	private List<PageInfoBean> initPageInfoBeanList(String pageInfo) throws JSONException {

		List<PageInfoBean> list = new ArrayList<PageInfoBean>();

		if (pageInfo != null) {
			JSONArray array = new JSONArray(pageInfo);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				int orderInList = object.getInt("orderInList");
				String pageName = object.getString("pageName");
				String pageURL = object.getString("pageURL");
				JSONArray subJsonArray = new JSONArray(
						object.getString("subPageList"));

				ArrayList<HashMap<String, String>> subPageList = new ArrayList<HashMap<String, String>>();

				for (int j = 0; j < subJsonArray.length(); j++) {

					JSONObject subJsonObject = subJsonArray.getJSONObject(j);

					HashMap<String, String> subHashMap = new HashMap<String, String>();
					subHashMap.put("subPageName",
							subJsonObject.getString("subPageName"));
					subHashMap.put("subPageURL",
							subJsonObject.getString("subPageURL"));

					subPageList.add(subHashMap);
				}
				list.add(new PageInfoBean(orderInList, pageName, pageURL, subPageList));
			}
			return list;
		} else {
			return null;
		}
	}
	
	private void setSlidingMenuItemAfterPageSelected(final int arg0) {
		if (arrayOfSlidingmenuItemList.get(arg0).size() > 1 && arg0 != 0) {
			leftButton.setVisibility(View.VISIBLE);
			data = arrayOfSlidingmenuItemList.get(arg0);
		slidingmenuAdapter = new SimpleAdapter(MainActivity.this, data, R.layout.slidingmenu_item_layout, new String[]{"subPageName"}, new int[]{R.id.slidingmenu_item_textview});
			slidingmenuList.setAdapter(slidingmenuAdapter);
			slidingmenuList.setOnItemClickListener(new OnItemClickListener() {
				private int itemOrder = -1;

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
			
					if(itemOrder != -1){
						arg0.getChildAt(itemOrder).setBackgroundColor(Color.BLACK);
					}
				
					String subUrl =  pageInfoBeanList.get(viewPager.getCurrentItem()).getSubPageList().get(arg2).get("subPageURL");
					
					((TabFragment) mMyFragmentManager.getFragment(currentPage)).changePage(subUrl);
					arg1.setBackgroundColor(Color.GRAY);
					itemOrder  = arg2;
					slidingMenu.toggle();
				}
			});
		}else{
			leftButton.setVisibility(View.GONE);
		}
	}
	
	
	 @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	            exit();
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	    }

	    public void exit() {
	        if ((System.currentTimeMillis() - exitTime) > 2000) {
	            Toast.makeText(this, getResources().getString(R.string.press_again_to_exit),
	                    Toast.LENGTH_SHORT).show();
	            exitTime = System.currentTimeMillis();
	        } else {
	            finish();
	        }
	    }
	
	    private void showPopupWindow(View view) {

	        View contentView = LayoutInflater.from(this).inflate(
	                R.layout.search_dialog, null);
	        
	        final PopupWindow popupWindow = new PopupWindow(contentView,
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
	        
	        SearchView searchView = (SearchView) contentView.findViewById(R.id.search_view);
	        searchView.setIconified(false);
	        
	        searchView.setOnQueryTextListener(new OnQueryTextListener() {
				
				@Override
				public boolean onQueryTextSubmit(String arg0) {
					if (!TextUtils.isEmpty(arg0)) {
					Intent intent = new Intent(MainActivity.this, SearchActivity.class);
					intent.putExtra("key_word", arg0);
					startActivity(intent);
					if (popupWindow != null) {
						popupWindow.dismiss();
					}
					}
					
					return false;
				}
				
				@Override
				public boolean onQueryTextChange(String arg0) {
					return false;
				}
			});
	        
	        searchView.setOnCloseListener(new OnCloseListener() {
				
				@Override
				public boolean onClose() {
					if (popupWindow != null) {
						popupWindow.dismiss();
					}
					return false;
				}
			});
	        popupWindow.setTouchable(true);

	        popupWindow.setBackgroundDrawable(getResources().getDrawable(
	                R.drawable.search_view_popup_background));

	        popupWindow.showAsDropDown(view, 0, DimensionUtils.pxToDp(this, 42));

	    }

}
