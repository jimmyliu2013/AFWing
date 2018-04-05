package com.lim.afwing.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lim.afwing.R;
import com.lim.afwing.activitys.Constant;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexListAdapter extends TabListAdapter{

	public IndexListAdapter(Context context, Cursor c) {
		super(context, c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount() - Constant.HEADER_ITEM_COUNT;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		cursor.moveToPosition(cursor.getPosition() + Constant.HEADER_ITEM_COUNT);
		super.bindView(view, context, cursor);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		mCursor.moveToPosition(position + Constant.HEADER_ITEM_COUNT);
		
		return TabListItemBean.fromCursor(mCursor);
	}


}
