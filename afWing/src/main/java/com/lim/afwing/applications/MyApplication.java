package com.lim.afwing.applications;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class MyApplication extends Application {

	private static String fontSize;
	private static boolean nightMode;
	private static boolean wifiOnly;
	
	private static boolean isWifiConnected;
	
	private static boolean isConnected;

	public static boolean isConnected() {
		return isConnected;
	}

	public static void setConnected(boolean isConnected) {
		MyApplication.isConnected = isConnected;
	}

	private static Context mContext;
	
	public static Context getGlobalContext(){
		return mContext;
	}
	
	public static boolean isWifiConnected() {
		return isWifiConnected;
	}

	public static void setWifiConnected(boolean isWifiConnected) {
		MyApplication.isWifiConnected = isWifiConnected;
	}

	public static String getFontSize() {
		return fontSize;
	}

	public static void setFontSize(String fontSize) {
		MyApplication.fontSize = fontSize;
	}

	public static boolean isNightMode() {
		return nightMode;
	}

	public static void setNightMode(boolean nightMode) {
		MyApplication.nightMode = nightMode;
	}

	public static boolean isWifiOnly() {
		return wifiOnly;
	}

	public static void setWifiOnly(boolean wifiOnly) {
		MyApplication.wifiOnly = wifiOnly;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mContext = getApplicationContext();
		loadSettings();
		loadWifiStatus();
		//isWifiConnected = false;

	}

	private void loadSettings() {
		SharedPreferences defaultSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		fontSize = defaultSharedPreferences.getString(
				"font_size", "3");
		nightMode = defaultSharedPreferences.getBoolean("night_mode", false);
		wifiOnly = defaultSharedPreferences.getBoolean("wifi_only", false);
	}

	private void loadWifiStatus(){
		 ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
	        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);  
	        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);  
	        NetworkInfo activeInfo = manager.getActiveNetworkInfo();  
	        isWifiConnected = wifiInfo.isConnected();
	        isConnected = (wifiInfo.isConnected() || mobileInfo.isConnected());
	}
	
}
