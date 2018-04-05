package com.lim.afwing.dao;

public class Contract {

	public static final int COUNT_OF_TABLE = 6;
	
	public static final String DB_NAME = "AFWing.db";
	
	public static final class ColumnName{
		
//		public String imageUrl;
//		public String title;
//		public String brefing;
//		public String tips;
//		public String linkUrl;
		public static final String ID = "_id";
	    public static final String IMAGE_URL = "imageUrl";
	    public static final String TITLE = "title";
	    public static final String BREFING = "brefing";
	    public static final String TIPS = "tips";
	    public static final String LINK_URL = "linkUrl";
	}
	
	public static final class HeaderColumnName{
		
		public static final String IMAGE_URL = "imageUrl";
	    public static final String TITLE = "title";
	    public static final String LINK_URL = "linkUrl";
		
	}
	
	public static String getTableName(int order){
		return "_" + order;
	}
	
}
