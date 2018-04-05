package com.lim.afwing.dao;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jsoup.parser.ParseError;

import com.lim.afwing.beans.PageInfoBean;
import com.lim.afwing.utils.CommonUtils;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;


/**
 * Created by storm on 14-4-8.
 */
public class DataProvider extends ContentProvider {
    static final String TAG = DataProvider.class.getSimpleName();

    static final Object DBLock = new Object();

    public static final String AUTHORITY = "com.lim.afwing.provider";

    public static final String SCHEME = "content://";

    // messages
    public static final String PATH_ITEMS = "/items";

    public static final Uri ITEMS_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY + PATH_ITEMS);


    private static final UriMatcher uriMatcher;

	public static final String FEEDS_CONTENT_URI_ROOT = SCHEME + AUTHORITY + "/";
    
    private MySQLiteOpenHelper dbHelper;

	private SQLiteDatabase db;
	

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (int i = 0; i < Contract.COUNT_OF_TABLE; i++) {
			uriMatcher.addURI(AUTHORITY, Contract.getTableName(i), i);
		}
    }

    
    

    public DataProvider() {
		super();
	}

	@Override
    public boolean onCreate() {
        
    	dbHelper = new MySQLiteOpenHelper(getContext(), Contract.DB_NAME, null, 1);
    	db = dbHelper.getWritableDatabase();
    	return true;
        
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (DBLock) {
        	
        	
        	
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            int table = uriMatcher.match(uri);
            
            String tableName = parseName(table);
            
            queryBuilder.setTables(tableName);

            Cursor cursor = queryBuilder.query(db, // The database to
                    // queryFromDB
                    projection, // The columns to return from the queryFromDB
                    selection, // The columns for the where clause
                    selectionArgs, // The values for the where clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    sortOrder // The sort order
            );

            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            //db.close();
            return cursor;
        }
    }

    private String parseName(int table) {
		// TODO Auto-generated method stub
    	return Contract.getTableName(table);
//    	switch (table) {
//		case MAIN:
//			return getContext().getResources().getString(R.string.main);
//		case CROSS:
//			return getContext().getResources().getString(R.string.cross);
//		case UNSCIENTIFIC:
//			return getContext().getResources().getString(R.string.unscientific);
//		case GIF:
//			return getContext().getResources().getString(R.string.gif);
//		case CUTE:
//			return getContext().getResources().getString(R.string.cute);
//		case MEIZI:
//			return getContext().getResources().getString(R.string.meizi);
//		case TWOD:
//			return getContext().getResources().getString(R.string.twod);
//		case NSFW:
//			return getContext().getResources().getString(R.string.nsfw);
//		case FAVORITE:
//			return getContext().getResources().getString(R.string.favorite);
//			//break;
//
//		default:
//			throw new IllegalArgumentException("Unknown table ");
//		}
	}

	@Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + uriMatcher.match(uri);
		
//		switch (uriMatcher.match(uri)) {
//            
//            case MAIN:
//                return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.main);
//            case CROSS:
//            	return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.cross);
//            case UNSCIENTIFIC:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.unscientific);
//    		case GIF:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.gif);
//    		case CUTE:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.cute);
//    		case MEIZI:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.meizi);
//    		case TWOD:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.twod);
//    		case NSFW:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.nsfw);
//    		case FAVORITE:
//    			return "vnd.android.cursor.dir/vnd.com.lim.afwing.provider." + getContext().getResources().getString(R.string.favorite);
//            default:
//                throw new IllegalArgumentException("Unknown URI " + uri);
//        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBLock) {
        	int table = uriMatcher.match(uri);
            String tableName = parseName(table);
            //SQLiteDatabase db = dbHelper.getReadableDatabase();
            long rowId = 0;
            db.beginTransaction();
            try {
                rowId = db.insertOrThrow(tableName, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            } finally {
                db.endTransaction();
            }
            if (rowId > 0) {
                Uri returnUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
        	//SQLiteDatabase db = dbHelper.getReadableDatabase();

            int count = 0;
            int table = uriMatcher.match(uri);
            String tableName = parseName(table);
            db.beginTransaction();
            try {
                count = db.delete(tableName, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
        	//SQLiteDatabase db = dbHelper.getReadableDatabase();
            int count;
            int table = uriMatcher.match(uri);
            String tableName = parseName(table);
            db.beginTransaction();
            try {
                count = db.update(tableName, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }

	public static void deleteAllDatabases() {
		// TODO Auto-generated method stub
		CommonUtils.deleteFilesByDirectory(new File("/data/data/com.lim.afwing/databases"));
	}

    
}

