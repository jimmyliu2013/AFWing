package com.lim.afwing.activitys;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.lim.afwing.R;
import com.lim.afwing.beans.PageInfoBean;
import com.lim.afwing.utils.CacheUtils;
import com.lim.afwing.utils.CommonUtils;
import com.lim.afwing.utils.DownLoadManager;
import com.lim.afwing.utils.HtmlParser;
import com.lim.afwing.utils.UpdateInfo;
import com.lim.afwing.utils.UpdateInfoParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	private Handler handler;
	private UpdateInfo info = null;
	protected static final int UPDATE_CLIENT = 4;
	protected static final int GET_UPDATEINFO_ERROR = 5;
	protected static final int DOWN_ERROR = 6;
	protected static final int INSERT_SDCARD = 8;

	private final int SUCCESS = 0;
	private final int FAIL = 1;
	protected final int GETFROMCACHE = 2;
	protected final int GETFROMWEB = 3;
	private CacheUtils cacheUtils;
	protected final int CHECKUPDATE = 7;
	private String pageInfo;
	protected boolean getPageInfoSuccess = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean firstTime = defaultSharedPreferences.getBoolean("v2.0_first_time", true);
		if (firstTime) {
			Log.i("AFWing", "2.0 for the first time, clear preference");
			CommonUtils.deleteFilesByDirectory(new File("/data/data/"
					+ this.getPackageName() + "/shared_prefs"));
			SharedPreferences defaultSharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this);
			defaultSharedPreferences1.edit().putBoolean("v2.0_first_time", false).commit();
		}
		

		
		
		cacheUtils = new CacheUtils(this, "settingsCache");

		
		
		pageInfo = null;

		final TextView textView = (TextView) findViewById(R.id.state_text);
		textView.setText(getResources().getString(R.string.loading));
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {

				case GETFROMCACHE:
					textView.setText(getResources().getString(R.string.loading_cache));

					break;

				case GETFROMWEB:
					textView.setText(getResources().getString(R.string.getting_page_info));
					break;

				case SUCCESS:

					pageInfo = (String) msg.obj;
		

					break;
				case FAIL:
					textView.setText("");
						
					if (WelcomeActivity.this != null) {
						Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.load_page_info_failed),
								1).show();
					}
					handler = null;
					WelcomeActivity.this.finish();
					break;

				case CHECKUPDATE:
					textView.setText(getResources().getString(R.string.checking_update));
					break;

				case UPDATE_CLIENT:
					showUpdataDialog();
					break;
				case GET_UPDATEINFO_ERROR:
					if (pageInfo != null) {
						 LoginMain();
					}else {
						if (WelcomeActivity.this != null) {
						Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.load_page_info_failed), 1).show();
						}
						handler = null;
						WelcomeActivity.this.finish();
					}
					break;
					
				case INSERT_SDCARD:
					textView.setText(getResources().getString(R.string.insert_sd_card));
					Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.insert_sd_card), 0).show();
					break;
					
				case DOWN_ERROR:
					Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.failed_to_download_new_version), 0).show();
					if (pageInfo != null) {
						 LoginMain();
					}else {
						
						Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.load_page_info_failed), 1).show();
						WelcomeActivity.this.finish();
					}
					break;

				default:
					break;
				}
			}
		};


		new Thread(new Runnable() {

			@Override
			public void run() {

				// 从缓存中取
				Message messageFromCache = new Message();
				messageFromCache.what = GETFROMCACHE;
				handler.sendMessage(messageFromCache);
				textView.setText(getResources().getString(R.string.loading_cache));
				String fromCache = getPageInfoFromCache();
				if (fromCache != null && fromCache != "") {
					pageInfo = fromCache;


					Message messageCacheSuccess = new Message();
					messageCacheSuccess.what = SUCCESS;
					messageCacheSuccess.obj = fromCache;
					handler.sendMessage(messageCacheSuccess);
				} else {

					Message messageFromWeb = new Message();
					messageFromWeb.what = GETFROMWEB;
					handler.sendMessage(messageFromWeb);
					
					try {
						List<PageInfoBean> list = HtmlParser.getPageInfoFromWeb();
						list.remove(list.size()-1);
						//list.remove(1);
						String fromWeb = changePageInfoFromListToString(list);
						if (fromWeb != null && fromWeb != "") {
							pageInfo = fromWeb;
							putPageInfoToCache(fromWeb);
							Message messageWebSuccess = new Message();
							messageWebSuccess.what = SUCCESS;
							messageWebSuccess.obj = fromWeb;
							handler.sendMessage(messageWebSuccess);
						}

					} catch (IOException | JSONException e) {
						e.printStackTrace();
						Message messageFail = new Message();

						messageFail.what = FAIL;
						handler.sendMessage(messageFail);
					}
				}
				try {

					Message messageUpdate = new Message();
					messageUpdate.what = CHECKUPDATE;
					handler.sendMessage(messageUpdate);

					
					//String path = "http://paradisecity.bj.bcebos.com/update/afwing_update.xml?responseContentDisposition=attachment";// getResources().getString(R.string.serverurl);
					
					URL url = new URL(Constant.UPDATE_INFO_PATH);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(10000);
					InputStream is = conn.getInputStream();
					info = UpdateInfoParser.getUpdateInfo(is);

					String versionName = getVersionName();
					if (info.getVersion().equals(versionName)) { // versionname������һ������
						if (pageInfo != null) {
							 LoginMain();
						}else {
							WelcomeActivity.this.finish();
//							Toast.makeText(WelcomeActivity.this, "没有得到页面信息，请检查网络或清除缓存后重试！", 0).show();
						}
					} else {
						Message msg = new Message();
						msg.what = UPDATE_CLIENT;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = GET_UPDATEINFO_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}

			}
		}).start();
		

	}


	public String getPageInfoFromCache() {
		return cacheUtils.getString("pageInfoBeanList");

	}

	public void putPageInfoToCache(String info) {
		cacheUtils.putString("pageInfoBeanList", info);
	}

	public String changePageInfoFromListToString(List<PageInfoBean> list)
			throws JSONException {


		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {

			PageInfoBean pageInfo = list.get(i);

			JSONObject object = new JSONObject();
			object.put("orderInList", pageInfo.getOrderInList());
			object.put("pageName", pageInfo.getPageName());
			object.put("pageURL", pageInfo.getPageURL());

			List<HashMap<String, String>> subPageList = pageInfo
					.getSubPageList();

			JSONArray subJsonArray = new JSONArray();

			for (int j = 0; j < subPageList.size(); j++) {
				HashMap<String, String> subHashMap = subPageList.get(j);
				JSONObject subObject = new JSONObject();
				subObject.put("subPageName", subHashMap.get("subPageName"));
				subObject.put("subPageURL", subHashMap.get("subPageURL"));
				subJsonArray.put(subObject);
			}


			object.put("subPageList", subJsonArray);

			jsonArray.put(object);

		}

		return jsonArray.toString();

	}

	private String getVersionName() throws Exception {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
	}


	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setTitle(getResources().getString(R.string.update_title));
		builer.setMessage(info.getDescription());
		builer.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//Log.i("LoginActivity", "download apk");
				downLoadApk();
			}
		});
		builer.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (pageInfo != null) {
					 LoginMain();
				}else {
					WelcomeActivity.this.finish();
					Toast.makeText(WelcomeActivity.this,getResources().getString(R.string.load_page_info_failed), 1).show();
				}
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	protected void downLoadApk() {
		final ProgressDialog pd; // ������Ի���?
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage(getResources().getString(R.string.downloading_new_version));
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							info.getUrl(), pd);
					sleep(1000);
					if (file != null) {

						installApk(file);
					} else {
						Message msg = new Message();
						msg.what = INSERT_SDCARD;
						handler.sendMessage(msg);
						
					}
					pd.dismiss(); // �����������Ի���
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		this.finish();
	}

	
	  private void LoginMain() { 
	
	  
		Intent intent = new Intent(WelcomeActivity.this,
				MainActivity.class);
		intent.putExtra("pageInfoBeanList", pageInfo);
		startActivity(intent);
		WelcomeActivity.this.finish();
	  }
	 

}
