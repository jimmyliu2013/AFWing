package com.lim.afwing.beans;

import android.database.Cursor;

import com.lim.afwing.dao.Contract;

public class HeaderViewPagerBean {

	public String linkUrl;
	public String imageUrl;
	public String title;
	
	public HeaderViewPagerBean(String linkUrl, String imageUrl, String title) {
		this.linkUrl = linkUrl;
		this.imageUrl = imageUrl;
		this.title = title;

	}
	
	public static HeaderViewPagerBean fromCursor(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.TITLE));
        String linkUrl = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.LINK_URL));
        String imageUrl = cursor.getString(cursor.getColumnIndex(Contract.ColumnName.IMAGE_URL));
        
        HeaderViewPagerBean item = new HeaderViewPagerBean(linkUrl, imageUrl, title);
        return item;
    }
}
