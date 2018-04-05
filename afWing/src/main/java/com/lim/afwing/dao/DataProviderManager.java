package com.lim.afwing.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.TabListItemBean;

import android.support.v4.content.CursorLoader;

/**
 * Created by storm on 14-4-8.
 */
public class DataProviderManager extends BaseDataProviderManager {
    private static final String TAG = "DataProviderManager";
	private String mTabName;
	//private String[] mTabNames;
   // private Context mContext;
    
    
    public DataProviderManager(Context context, String tabName) {
        super(context);
    	//mContext = context;
    	mTabName = tabName;
    	//mTabNames = tabNames;
    	
    }

    @Override
    protected Uri getContentUri() {
        return Uri.parse(DataProvider.FEEDS_CONTENT_URI_ROOT + mTabName) ;
    }

    
    
//    public static final String ID = "_id";
//	public static final String TITLE = "title";
//	public static final String LINK_URL = "linkUrl";
//	public static final String THUMBNAIL_URL = "thumbnailUrl";
//	public static final String IMAGE_URL = "imageUrl";
//	public static final String LIKE_RATE = "likeRate";
//	public static final String COMMENT_NUMBER = "commentNumber";
//	public static final String SCALE = "scale";
//	public static final String IS_EXPANDABLE = "isExpandable";

    private ContentValues getContentValues(TabListItemBean item) {
        ContentValues values = new ContentValues();
        //values.put(Contract.ColumnName.ID, item.id);
        values.put(Contract.ColumnName.TITLE, item.title);
        values.put(Contract.ColumnName.LINK_URL, item.linkUrl);
        values.put(Contract.ColumnName.IMAGE_URL, item.imageUrl);
        values.put(Contract.ColumnName.BREFING, item.brefing);
        values.put(Contract.ColumnName.TIPS, item.tips);
//         values.put(MySQLiteOpenHelper.IS_EXPANDABLE, item.isExpandable);
        return values;
    }

    public TabListItemBean query(long id) {
    	TabListItemBean item = null;
        Cursor cursor = query(null, Contract.ColumnName.ID + "= ?",
                new String[] {String.valueOf(id)}, null);
        if (cursor.moveToFirst()) {
        	item = TabListItemBean.fromCursor(cursor);
        }
        cursor.close();
        return item;
    }

    public void bulkInsert(List<TabListItemBean> itemList) {
        ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
        for (TabListItemBean item : itemList) {
            ContentValues values = getContentValues(item);
            contentValues.add(values);
        }
        ContentValues[] valueArray = new ContentValues[contentValues.size()];
        bulkInsert(contentValues.toArray(valueArray));
    }

    public int deleteAll() {
        synchronized (DataProvider.DBLock) {
        	MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(getContext(), Contract.DB_NAME, null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int row = db.delete(mTabName, null, null);
            db.close();
            return row;
        }
    }
    
    public static void clearDatabase() {
        synchronized (DataProvider.DBLock) {
        	MySQLiteOpenHelper dbHelper = new MySQLiteOpenHelper(MyApplication.getGlobalContext(), Contract.DB_NAME, null, 1);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            for (int i = 0; i < Contract.COUNT_OF_TABLE; i++) {
            	db.delete(Contract.getTableName(i), null, null);
			}
            
            db.close();
            //return row;
        }
    }

    public CursorLoader getCursorLoader() {
        return new CursorLoader(getContext(), getContentUri(), null, null, null, null);//MySQLiteOpenHelper.ID + " DESC");
        
    }

   
}
