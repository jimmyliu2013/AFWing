package com.lim.afwing.activitys;

import java.io.File;

import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.lim.afwing.R;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.dao.Contract;
import com.lim.afwing.dao.DataProviderManager;
import com.lim.afwing.utils.CommonUtils;
import com.lim.afwing.utils.DataUtils;
import com.lim.afwing.utils.DimensionUtils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatPreferenceActivity implements
		OnPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true); // 决定左上角图标的右侧是否有向左的小箭头, true
		actionBar.setDisplayShowHomeEnabled(false);

		addPreferencesFromResource(R.xml.settings);
		findPreference("font_size").setOnPreferenceChangeListener(this);
		findPreference("night_mode").setOnPreferenceChangeListener(this);
		findPreference("wifi_only").setOnPreferenceChangeListener(this);

		getListView().setBackgroundColor(
				getResources().getColor(R.color.day_mode_background));

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		layoutParams.topMargin = DimensionUtils.dpToPx(this, 24);
		layoutParams.bottomMargin = DimensionUtils.dpToPx(this, 24);
		getListView().setLayoutParams(layoutParams);
	}

	@Override
	protected void onResume() {

		Log.d("lim", "settings activity on resume");
		CommonUtils.setActivityBackground(this, MyApplication.isNightMode());
		//findPreference("clear_content_cache").setSummary(getContentCacheSize() + "KB");
		findPreference("clear_picture_cache").setSummary(getPictureCacheSize() + "MB");
		
		
		super.onResume();
	}

	@Override
	@Deprecated
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference.getKey().equals("font_size")) {

		} else if (preference.getKey().equals("night_mode")) {

		} else if (preference.getKey().equals("clear_content_cache")) {
			AlertDialog.Builder builder1 = new Builder(SettingsActivity.this);
			builder1.setTitle(getResources().getString(
					R.string.confirm_to_clear_content_cache));
			builder1.setPositiveButton(
					getResources().getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							cleanContentCache();
						}
					});
			builder1.setNegativeButton(getResources()
					.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog dialog1 = builder1.show();
			dialog1.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					//findPreference("clear_content_cache").setSummary(getContentCacheSize() + "KB");
				}
			});
		} else if (preference.getKey().equals("clear_picture_cache")) {
			AlertDialog.Builder builder = new Builder(SettingsActivity.this);
			builder.setTitle(getResources().getString(
					R.string.confirm_to_clear_image_cache));
			builder.setPositiveButton(getResources()
					.getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							cleanImageCache();
						}
					});
			builder.setNegativeButton(
					getResources().getString(R.string.cancel),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			AlertDialog dialog = builder.show();
			dialog.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					findPreference("clear_picture_cache").setSummary(getPictureCacheSize() + "MB");
				}
			});
		} else if (preference.getKey().equals("wifi_only")) {

		} else if (preference.getKey().equals("info")) {
			AlertDialog.Builder builder = new Builder(SettingsActivity.this);
			builder.setTitle(getResources().getString(R.string.info_title));
			builder.setMessage(getResources().getString(R.string.info));
			builder.setPositiveButton(getResources()
					.getString(R.string.confirm),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							cleanImageCache();
						}
					});

			builder.show();
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:// 点击返回图标事件
			this.finish();
			break;
		default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void cleanContentCache() {
		CommonUtils.deleteFilesByDirectory(new File("/data/data/"
				+ SettingsActivity.this.getPackageName() + "/shared_prefs"));
		//deleteDatabase(Contract.DB_NAME);
		//DataProviderManager.clearDatabase();
	}

	private void cleanImageCache() {
		CommonUtils
				.deleteFilesByDirectory(new File(getCacheDir()
						.getAbsoluteFile()
						+ "/"
						+ Constant.IMAGE_CACHE_DIR_NAME + "/"));
		CommonUtils.deleteFilesByDirectory(new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ Constant.IMAGE_CACHE_DIR_NAME + "/"));
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if (key.equals("night_mode")) {
			MyApplication.setNightMode((boolean) newValue);
			CommonUtils.setActivityBackground(this, (boolean) newValue);

		} else if (key.equals("wifi_only")) {
			MyApplication.setWifiOnly((boolean) newValue);
		} else if (key.equals("font_size")) {
			MyApplication.setFontSize((String) newValue);
		}

		return true;
	}

	private long getContentCacheSize() {
		long folderSize = DataUtils.getFolderSize(new File(
				"/data/data/com.lim.afwing/databases"));

		folderSize = folderSize / (1024);

		if (folderSize <= 100) {
			return 0;
		}
		
		folderSize = folderSize - 100;
		return folderSize;
	}

	private long getPictureCacheSize() {

		String externalPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + Constant.IMAGE_CACHE_DIR_NAME + "/";
		String internalPath = getCacheDir().getAbsolutePath() + "/"
				+ Constant.IMAGE_CACHE_DIR_NAME + "/";

		/*
		 * dlw = DiskLruCacheWrapper.get(new File(Environment
		 * .getExternalStorageDirectory().getAbsolutePath() + "/" +
		 * Constant.IMAGE_CACHE_DIR_NAME + "/"), 50 * 1024 * 1024); } else { //
		 * 当前不可用 Log.i("AFWing", "create image cache dir on cache"); dlw =
		 * DiskLruCacheWrapper.get(new File(getCacheDir().getAbsolutePath() +
		 * "/" + Constant.IMAGE_CACHE_DIR_NAME + "/"), 30 * 1024 * 1024);
		 */

		long folderSize = DataUtils.getFolderSize(new File(externalPath))
				+ DataUtils.getFolderSize(new File(internalPath));
		folderSize = folderSize / (1024 * 1024);
		return folderSize;
	}

}
