package com.lim.afwing.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageInfoBean {
	private int orderInList;
	private String pageName;
	private String pageURL;
	private ArrayList<HashMap<String, String>> subPageList;
	
	
	public PageInfoBean(int orderInList, String pageName, String pageURL,
			ArrayList<HashMap<String, String>> subPageList) {
		//super();
		this.orderInList = orderInList;
		this.pageName = pageName;
		this.pageURL = pageURL;
		this.subPageList = subPageList;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageURL() {
		return pageURL;
	}
	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	public ArrayList<HashMap<String, String>> getSubPageList() {
		return subPageList;
	}
	public void setSubPageList(ArrayList<HashMap<String, String>> subPageList) {
		this.subPageList = subPageList;
	}
	public int getOrderInList() {
		return orderInList;
	}
	public void setOrderInList(int orderInList) {
		this.orderInList = orderInList;
	}

	

	
	}
	
	
	
	
	
	
	
	
	


