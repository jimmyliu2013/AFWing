package com.lim.afwing.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lim.afwing.beans.PageInfoBean;
import com.lim.afwing.beans.TabListItemBean;

import android.content.Context;
import android.content.SharedPreferences;

public class CacheUtils {

	private SharedPreferences sharedPreferences;

	public CacheUtils(Context context, String name) {

		sharedPreferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
	}

	public void putString(String key, String value) {

		sharedPreferences.edit().putString(key, value).commit();

	}

	public String getString(String key) {

		return sharedPreferences.getString(key, null);
	}

	public void clear() {
		sharedPreferences.edit().clear().commit();

	}

	public boolean getBoolean(String key) {

		return sharedPreferences.getBoolean(key, true);
	}

	public void putBoolean(String key, boolean value) {

		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	public void putTabItemBeanList(String key, List<TabListItemBean> tabListItem)
			throws JSONException {

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < tabListItem.size(); i++) {
			JSONObject object = new JSONObject();
			TabListItemBean tabItem = tabListItem.get(i);
			object.put("imageUrl", tabItem.getImageUrl());
			object.put("title", tabItem.getTitle());
			object.put("brefing", tabItem.getBrefing());
			object.put("tips", tabItem.getTips());
			object.put("linkUrl", tabItem.getLinkUrl());

			jsonArray.put(object);
		}

		sharedPreferences.edit().putString(key, jsonArray.toString()).commit();

	}

	public void putCurrentIndex(int currentIndex) {
		sharedPreferences.edit().putInt("currentIndex", currentIndex).commit();
	}

	public int getCurrentIndex() {

		return sharedPreferences.getInt("currentIndex", -1);

	}

	public void putCurrentUrl(String currentUrl) {
		sharedPreferences.edit().putString("currentUrl", currentUrl).commit();
	}

	public String getCurrentUrl() {

		return sharedPreferences.getString("currentUrl", null);

	}

	public List<TabListItemBean> getTabItemBeanList(String key)
			throws JSONException {

		String jsonString = sharedPreferences.getString(key, null);

		List<TabListItemBean> list = new ArrayList<TabListItemBean>();

		if (jsonString != null) {
			JSONArray array = new JSONArray(jsonString);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				String imageUrl = object.getString("imageUrl");
				String title = object.getString("title");
				String brefing = object.getString("brefing");
				String tips = object.getString("tips");
				String linkUrl = object.getString("linkUrl");
				TabListItemBean tabItem = new TabListItemBean(0, imageUrl, title,
						brefing, tips, linkUrl);
				list.add(i, tabItem);
			}
			return list;
		} else {
			return null;
		}

	}

	public void putViewPagerInfoList(List<HashMap<String, String>> viewPagerList)
			throws JSONException {

		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < viewPagerList.size(); i++) {
			JSONObject object = new JSONObject();
			HashMap<String, String> map = viewPagerList.get(i);
			object.put("imageUrl", map.get("imageUrl"));
			object.put("description", map.get("description"));
			object.put("linkUrl", map.get("linkUrl"));

			jsonArray.put(object);
		}

		sharedPreferences.edit().putString("viewpager", jsonArray.toString())
				.commit();

	}

	public ArrayList<HashMap<String, String>> getViewPagerInfoList()
			throws JSONException {

		String jsonString = sharedPreferences.getString("viewpager", null);

		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if ((jsonString != null) && (jsonString != "")) {
			JSONArray array = new JSONArray(jsonString);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				String imageUrl = object.getString("imageUrl");
				String description = object.getString("description");
				String linkUrl = object.getString("linkUrl");
				map.put("imageUrl", imageUrl);
				map.put("description", description);
				map.put("linkUrl", linkUrl);
				list.add(i, map);
			}

			return list;
		} else {
			return null;
		}

	}

	public void putPageInfoBeanList(List<PageInfoBean> list)
			throws JSONException {

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {

			PageInfoBean pageInfo = list.get(i);

			JSONObject object = new JSONObject();
			object.put("orderInList", i);
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
		sharedPreferences.edit()
				.putString("pageInfoBeanList", jsonArray.toString()).commit();

	}

	public List<PageInfoBean> getPageInfoBeanList() throws JSONException {

		String jsonString = sharedPreferences.getString("pageInfoBeanList",
				null);

		List<PageInfoBean> list = new ArrayList<PageInfoBean>();

		if (jsonString != null) {
			JSONArray array = new JSONArray(jsonString);
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

}
