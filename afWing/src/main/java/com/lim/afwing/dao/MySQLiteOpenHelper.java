package com.lim.afwing.dao;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.lim.afwing.beans.PageInfoBean;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper{
	
//	public static final String ID = "_id";
//	public static final String TITLE = "title";
//	public static final String LINK_URL = "linkUrl";
//	public static final String THUMBNAIL_URL = "thumbnailUrl";
//	public static final String IMAGE_URL = "imageUrl";
//	public static final String LIKE_RATE = "likeRate";
//	public static final String COMMENT_NUMBER = "commentNumber";
//	public static final String SCALE = "scale";
//	public static final String IS_EXPANDABLE = "isExpandable";
	private static final String TAG = "MySQLiteOpenHelper";
	
	
//	public String title;
//	public String linkUrl;
//	public String thumbnailUrl;
//	public String imageUrl;
//	public String likeRate;
//	public String id;
//	public String commentNumber;
//	public float scale;
//	public boolean isExpandable;
	
			
	//private String[] mTableName;
	
	public MySQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
//		for (int i = 0; i < pageInfoBeanList.size(); i++) {
//			try {
//				tableName[i] = pageInfoBeanList.get(i).getPageName().getBytes("utf-8").toString();
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				throw new RuntimeException("can not transfer string with utf-8!");
//			}
//			Log.d(TAG, "tableName = : " + tableName[i]);
//		}
		
	}

	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String[] SQLArray = getDropTableSQLArray();
		db.beginTransaction();
		try{
			for (int i = 0; i < SQLArray.length; i++) {
		
			db.execSQL(SQLArray[i]);
		}
			onCreate(db);
			db.setTransactionSuccessful();
		}catch(SQLException e){
			throw new SQLException("failed to build new database!");
		}finally {
		    db.endTransaction();
		}
		
		
	}
	
	private String[] getDropTableSQLArray() {
		String[] sql = new String[Contract.COUNT_OF_TABLE];
          for (int i = 0; i < Contract.COUNT_OF_TABLE; i++) {
        	  sql[i] = "DROP TABLE IF EXISTS "+ Contract.getTableName(i);
		}
		return sql;
	}



	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String[] SQLArray = getCreateTableSQLArray();
		
		
		db.beginTransaction();
		try {
			
			for (int i = 0; i < SQLArray.length; i++) {
				//Log.d(TAG, SQLArray[i]);
				db.execSQL(SQLArray[i]);
			}
		
		db.setTransactionSuccessful();
		}catch (SQLException e) {
		    throw new SQLException("failed to build new database!");
		    // Do whatever you want here
		}
		finally {
		    db.endTransaction();
		}
	}
	
	
	
	
	
	
	private String[] getCreateTableSQLArray(){
		
		String[] tableName = new String[Contract.COUNT_OF_TABLE];
		
		for (int i = 0; i < Contract.COUNT_OF_TABLE; i++) {
			
//		    public static final String IMAGE_URL = "imageUrl";
//		    public static final String TITLE = "title";
//		    public static final String BREFING = "brefing";
//		    public static final String TIPS = "tips";
//		    public static final String LINK_URL = "linkUrl";
			
			
			tableName[i] = "CREATE TABLE IF NOT EXISTS "+ Contract.getTableName(i) +" ("
					//+"_id integer primary key autoincrement, "
					+ Contract.ColumnName.ID +" integer primary key autoincrement, " 
					+ Contract.ColumnName.TITLE + " text not null, "
					+ Contract.ColumnName.LINK_URL + " text not null, "
					+ Contract.ColumnName.IMAGE_URL + " text not null, "
					+ Contract.ColumnName.BREFING + " text, "
					+ Contract.ColumnName.TIPS + " text"
					+ ")";
			
						
		}
		
		return tableName;
		
	}
	
	
	
	
}
