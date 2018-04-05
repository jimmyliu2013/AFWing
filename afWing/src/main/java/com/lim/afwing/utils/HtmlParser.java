package com.lim.afwing.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import com.lim.afwing.beans.PageInfoBean;
import com.lim.afwing.beans.TabListItemBean;

public class HtmlParser {

	public static List<PageInfoBean> getPageInfoFromWeb() throws IOException {
		List<PageInfoBean> list = new ArrayList<PageInfoBean>();

		Document doc = getDocument("http://www.afwing.com/");
		Elements pageNameElements = doc.getElementsByClass("nav").select("li");
		Elements pageURLElements = doc.getElementsByClass("nav").select("a");

		for (int i = 0; i < pageNameElements.size() - 1; i++) {

			ArrayList<HashMap<String, String>> subPageList = new ArrayList<HashMap<String, String>>();
			int orderInList = i;
			String pageName = pageNameElements.get(i).text();
			String pageURL = //"http://www.afwing.com" +
					pageURLElements.get(i).attr("href");
			Document subPageDoc = getDocument(
					                //"http://www.afwing.com" +
									pageURLElements.get(i).attr("href"));
//			Jsoup
//					.connect(
//							"http://www.afwing.com"
//									+ pageURLElements.get(i).attr("href"))
//					.header("User-Agent",
//							"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
//					.get();
			Elements linkList = subPageDoc.getElementsByClass("link_list1")
					.select("li");

			for (int j = 0; j < linkList.size(); j++) {
				HashMap<String, String> subPageHashMap = new HashMap<>();
				subPageHashMap.put("subPageName", linkList.get(j).text());
				subPageHashMap.put("subPageURL", (linkList.get(j).select("a")
						.attr("href").split("/"))[2]
						+ "/");
				subPageList.add(subPageHashMap);

			}

			list.add(new PageInfoBean(orderInList, pageName, pageURL, subPageList));
		}

		return list;
	}


	public static int getPageNumberFromWeb(String url) throws IOException {
		int pageNumber = 1;

		Document doc = getDocument(url);

		String lastPageUrl = doc.getElementsByClass("r-paging-cur").text();
		String pageNumberString = "1";
		if (lastPageUrl != null && lastPageUrl != "") {

			if (lastPageUrl.contains("/")) {
				pageNumberString = lastPageUrl.split("/")[1];
			}
			pageNumber = Integer.parseInt(pageNumberString);
		} else {
		}

		return pageNumber;

	}

	public static List<TabListItemBean> loadListViewAdapterData(String url,
			int startNum) throws HttpStatusException, IOException {

		List<TabListItemBean> list = new ArrayList<>();


		Document doc = getDocument(url);

		Elements tabList = null;
		if (url.equals("http://www.afwing.com/")) {// 区分首页和第二页

			tabList = doc.getElementsByClass("content3").select("div").get(0)
					.getElementsByClass("left").select("ul").select("li");
		} else {
			tabList = doc.getElementsByClass("content").select("div").get(0)
					.getElementsByClass("left_list").select("ul").select("li");

		}

		for (int i = startNum; i < tabList.size(); i++) {
			int id = 0; // as primarykey later
			String imageUrl = "http://www.afwing.com"
					+ tabList.get(i).select("img").attr("src");
			String title = tabList.get(i).getElementsByClass("title").text();
			String brefing = tabList.get(i).select("p").text();
			String tips = tabList.get(i).getElementsByClass("tips").text();
			String linkUrl = "http://m.afwing.com"
					+ tabList.get(i).getElementsByClass("title").attr("href");

			list.add(new TabListItemBean(id, imageUrl, title, brefing, tips,
					linkUrl));
		}
		return list;
	}

	private void loadPagerViewAdapterData(
			ArrayList<HashMap<String, String>> viewpagerInfoList,
			boolean clearOldData, String url) throws IOException {

		if (clearOldData) {
			viewpagerInfoList.clear();
		}
		Document doc = getDocument(url);
		Elements pagerList = doc.getElementsByClass("picshow_img").select("ul")
				.select("li");

		for (int i = 0; i < pagerList.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String imageUrl = "http://www.afwing.com"
					+ pagerList.get(i).select("img").attr("src");
			String description = pagerList.get(i).select("a").attr("alt");

			String linkUrl = "http://m.afwing.com"
					+ pagerList.get(i).select("a").attr("href");

			map.put("imageUrl", imageUrl);
			map.put("description", description);
			map.put("linkUrl", linkUrl);

			viewpagerInfoList.add(i, map);

		}
	}

	public static List<TabListItemBean> loadListData(String url) throws IOException, HttpStatusException {

		List<TabListItemBean> list = new ArrayList<>();
		

		Document doc = getDocument(url);
		Elements tabList = doc.getElementsByClass("left_list").select("li");

		

		for (int i = 0; i < tabList.size() - 1; i++) {
			int id = 0; // as primarykey later
			String imageUrl = "http://www.afwing.com"
					+ tabList.get(i).select("img").attr("src");
			String title = tabList.get(i).getElementsByClass("title").text();
			String brefing = tabList.get(i).select("p").text();
			String tips = tabList.get(i).getElementsByClass("tips").text();
			String linkUrl = "http://m.afwing.com"
					+ tabList.get(i).select("a").attr("href");

            list.add(new TabListItemBean(id, imageUrl, title,
					brefing, tips, linkUrl));
		}

		return list;
	}
	
	public static List<TabListItemBean> loadPagerViewAdapterData(String url) throws IOException {

		List<TabListItemBean> list = new ArrayList<>();
		
		Document doc = getDocument(url);
		Elements pagerList = doc.getElementsByClass("picshow_img").select("ul")
				.select("li");

		for (int i = 0; i < pagerList.size(); i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			String imageUrl = "http://www.afwing.com"
					+ pagerList.get(i).select("img").attr("src");
			
			String title = pagerList.get(i).select("a").attr("alt");

			String linkUrl = "http://m.afwing.com"
					+ pagerList.get(i).select("a").attr("href");

			//TabListItemBean tabListItemBean = new TabListItemBean(0, imageUrl, title, "", "", linkUrl);

			list.add(new TabListItemBean(0, imageUrl, title, "", "", linkUrl));

		}
		return list;
	}
	
	
	public static List<TabListItemBean> loadIndexListViewAdapterData(String url, int startNum) throws HttpStatusException, IOException {

		List<TabListItemBean> list = new ArrayList<>();
		

		Document doc = getDocument(url);

		Elements tabList = null;
		if (url.equals("http://www.afwing.com/")) {// 区分首页和第二页

			tabList = doc.getElementsByClass("content3").select("div").get(0)
					.getElementsByClass("left").select("ul").select("li");
		} else {
			tabList = doc.getElementsByClass("content").select("div").get(0)
					.getElementsByClass("left_list").select("ul").select("li");

		}

		for (int i = startNum; i < tabList.size(); i++) {
			int id = 0;
			String imageUrl = "http://www.afwing.com"
					+ tabList.get(i).select("img").attr("src");
			String title = tabList.get(i).getElementsByClass("title").text();
			String brefing = tabList.get(i).select("p").text();
			String tips = tabList.get(i).getElementsByClass("tips").text();
			String linkUrl = "http://m.afwing.com"
					+ tabList.get(i).getElementsByClass("title").attr("href");

			list.add(new TabListItemBean(id, imageUrl, title,
					brefing, tips, linkUrl));
		}
		return list;
	}
	
	public static String getHtmlFromWeb(String url, boolean isNightMode, boolean loadImage) throws IOException {

		//Log.d(TAG, "-----------------------------------------------------------------------------------------------");
		Document doc = getDocument(url);
		//Response response = null;

		//Document doc = conn.get();

		Element content = doc.getElementById("artI");
//		if (isNightMode) {
//			content.attr("text", "#ffffff");
//			content.attr("bgcolor", "#404040");
//		}
		
		Elements font_size = content.getElementsByClass("font_ds");
		font_size.remove();
	//	Elements content = doc.getElementsByClass("content");
	    //System.out.println(content.html());
		//Log.d(TAG, "########################################################" + content.html());

		Elements img = content.select("img");
		for (int i = 0; i < img.size(); i++) {
			String rawUrl = img.get(i).attr("src");
			
			
			
			if (!rawUrl.contains("http:")) {
				img.get(i).attr("src", "http://www.afwing.com" + rawUrl);
			}
			
			if (!loadImage) {
				img.get(i).attr("src", "file:///android_asset/placeholder_big.png");
			}
		}

		String html = content.html();

		if (isNightMode) {
			html = "<html><head><style>img{max-width: 100%; width:auto; height: auto;}</style><meta http-equiv=\"Content-Language\" content=\"zh-cn\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body bgcolor=\"#404040\" text=\"#ffffff\">"
					+ html + "</body></html>";
		}else {
			html = "<html><head><style>img{max-width: 100%; width:auto; height: auto;}</style><meta http-equiv=\"Content-Language\" content=\"zh-cn\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
					+ html + "</body></html>";
		}
		
		
		
		return html;
	}


	
	public static List<HashMap<String, String>> getResultFromWeb(String url)
			throws IOException {

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		Document doc = getDocument(url);

		Elements elements = doc.getElementsByClass("b_algo").select("h2");

		for (int i = 0; i < elements.size(); i++) {

			HashMap<String, String> map = new HashMap<String, String>();
			String title = elements.get(i).text();
			String linkUrl = elements.get(i).select("a").attr("href");
			map.put("title", title);
			System.out.println(title);
			map.put("linkUrl", linkUrl);
			System.out.println(linkUrl);
			if (linkUrl.contains("http://m.afwing.com/")) {
				list.add(map);
			}
			
		}
		return list;
	}
	
	private static Document getDocument(String url) throws IOException {
		Document doc = Jsoup
				.connect(url)
				.timeout(50000)
				.header("User-Agent",
						"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21").get();
		return doc;
	}
}
