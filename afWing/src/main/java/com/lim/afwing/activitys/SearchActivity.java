package com.lim.afwing.activitys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lim.afwing.R;
import com.lim.afwing.adapter.SearchResultListAdapter;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.utils.CommonUtils;
import com.lim.afwing.utils.HtmlParser;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchActivity extends ActionBarActivity {

	private List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
	private ListView listView;
//	private final int SUCCESS = 0;
//	private final int FAIL = 1;
//	private final int END = 2;
	public boolean isRefreshing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		Intent intent = getIntent();
		String keyWord = intent.getExtras().getString("key_word");
		setContentView(R.layout.activity_searchview_layout);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		listView = (ListView) findViewById(R.id.search_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchActivity.this,
						WebViewActivity.class);
				intent.putExtra("linkUrl", dataList.get(arg2).get("linkUrl"));
				startActivity(intent);

			}
		});
		
		new SearchTask().execute(keyWord);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			SearchActivity.this.finish();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	
	private class SearchTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			isRefreshing = true;
		}

		@Override
		protected Integer doInBackground(String... params) {

			String encoded = null;
			try {
				encoded = URLEncoder.encode(params[0],
						"utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.out.println(encoded);
			String url = "http://cn.bing.com/search?q="
					+ encoded
					+ "+site%3Am.afwing.com";
			
			/*String url = "http://www.sogou.com/sogou?query="
					+ encoded
					+ "+site%3Am.afwing.com";*/
			
			
			System.out.println(url);
			try {
				dataList = HtmlParser.getResultFromWeb(url);
				System.out.println(dataList);

				if (dataList.size() == 0) {
					return Constant.END;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Constant.FAIL;
			}
			return Constant.SUCCESS;
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case Constant.SUCCESS:

				SimpleAdapter adapter = new SearchResultListAdapter(SearchActivity.this,
						dataList, R.layout.search_list_item,
						new String[] { "title" }, new int[] { R.id.text });
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();

				break;
			case Constant.END:
				Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_end_toast_text),
						Toast.LENGTH_LONG).show();

				break;
			case Constant.FAIL:
				Toast.makeText(SearchActivity.this, getResources().getString(R.string.search_fail_toast_text),
						Toast.LENGTH_LONG).show();

				break;

			default:
				break;
			}
			isRefreshing = false;
		}

		
	}
	
	@Override
	protected void onResume() {
		
		CommonUtils.setActivityBackground(this, MyApplication.isNightMode());
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
