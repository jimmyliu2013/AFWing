package com.lim.afwing.activitys;

import java.io.IOException;

import com.lim.afwing.R;
import com.lim.afwing.adapter.ActivityWebPagerViewPagerAdapter;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.utils.CommonUtils;
import com.lim.afwing.utils.HtmlParser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class WebViewActivity extends ActionBarActivity {

	private String mUrl;
	private int pageNumber = 1;
	private SharedPreferences sp;
	private ActivityWebPagerViewPagerAdapter pagerAdapter;
	private ViewPager viewPager;

	private View emptyView;

	private View loadingView;

	private TextView showPageNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//sp = getSharedPreferences("size", Context.MODE_PRIVATE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(R.layout.actionbar_custom_layout_title);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		setContentView(R.layout.activity_web_view);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		mUrl = extras.getString("linkUrl");

		viewPager = (ViewPager) findViewById(R.id.vp_activity_web_view);

		showPageNumber = (TextView) findViewById(R.id.tv_activity_web_view_page_number);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				changeShowPageNumber((arg0 + 1) + "/" + pageNumber + getResources().getString(R.string.page));
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		loadingView = findViewById(R.id.ll_loading);

		emptyView = findViewById(R.id.ll_empty);

		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new GetPageNumberAsyncTask().execute(mUrl);
			}
		});

		new GetPageNumberAsyncTask().execute(mUrl);

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		
		case android.R.id.home:
			this.finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		
		CommonUtils.setActivityBackground(this, MyApplication.isNightMode());
		super.onResume();
	}
	
	
	public void changeShowPageNumber(String numString) {

		showPageNumber.setText(numString);

	}


	private class GetPageNumberAsyncTask extends
			AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			loadingView.setVisibility(View.VISIBLE);
			viewPager.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);

		}

		@Override
		protected Integer doInBackground(String... params) {

			try {
				pageNumber = HtmlParser.getPageNumberFromWeb(params[0]);

				return Constant.SUCCESS;

			} catch (IOException e) {
				e.printStackTrace();
				return Constant.FAIL;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {

			case Constant.SUCCESS:

				pagerAdapter = new ActivityWebPagerViewPagerAdapter(
						getSupportFragmentManager(), pageNumber, mUrl);
				if (viewPager != null) {
					viewPager.setAdapter(pagerAdapter);
				}

				changeShowPageNumber("1" + "/" + pageNumber + getResources().getString(R.string.page));

				loadingView.setVisibility(View.GONE);
				viewPager.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);

				break;

			case Constant.FAIL:
				loadingView.setVisibility(View.GONE);
				viewPager.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);

				break;

			default:
				break;

			}

		}

	}

}
