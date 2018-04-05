package com.lim.afwing.beans;

import com.lim.afwing.dao.Contract;

import android.R.integer;
import android.database.Cursor;

public class TabListItemBean {
	
	public int id;
	public String imageUrl;
	public String title;
	public String brefing;
	public String tips;
	public String linkUrl;
 
	
	
	
	public TabListItemBean(int id, String imageUrl, String title, String brefing,
			String tips,String linkUrl) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.title = title;
		this.brefing = brefing;
		this.tips = tips;
		this.linkUrl = linkUrl;

	}


	public String getLinkUrl() {
		return linkUrl;
	}


	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getBrefing() {
		return brefing;
	}


	public void setBrefing(String brefing) {
		this.brefing = brefing;
	}


	public String getTips() {
		return tips;
	}


	public void setTips(String tips) {
		this.tips = tips;
	}
	
	public static TabListItemBean fromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Contract.ColumnName.ID));
        String title = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.TITLE));
        String linkUrl = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.LINK_URL));
        String brefing = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.BREFING));
        String imageUrl = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.IMAGE_URL));
        String tips = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.TIPS));
        //boolean isExpandable = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.IS_EXPANDABLE)) != 0;
        
        TabListItemBean item = new TabListItemBean(id, imageUrl, title, brefing, tips, linkUrl);
        		//(title, linkUrl, thumbnailUrl, imageUrl, likeRate, id, commentNumber, scale, isExpandable);
        return item;
    }

	
	
}
