package com.lim.afwing.adapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lim.afwing.R;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TabListAdapter extends CursorAdapter{

	
	
	public TabListAdapter(Context context, Cursor c) {
		super(context, c);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.imageView.setImageResource(R.drawable.placeholder_small);
		if (MyApplication.isWifiOnly()) {
			if (MyApplication.isWifiConnected()) {
				
				loadPicture(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.IMAGE_URL)), viewHolder.imageView);
			}else if (!MyApplication.isConnected()) {
				loadPicture(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.IMAGE_URL)), viewHolder.imageView);
			}
		}else{
			loadPicture(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.IMAGE_URL)), viewHolder.imageView);
		}
		

		viewHolder.title.setText(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.TITLE)));
		viewHolder.brefing.setText(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.BREFING)));
		viewHolder.tips.setText(cursor.getString(cursor.getColumnIndex(Contract.ColumnName.TIPS)));
		
		if (MyApplication.isNightMode()) {
			view.setBackgroundColor((mContext.getResources().getColor(R.color.night_mode_background)));
			viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.night_mode_text_primary));
			viewHolder.brefing.setTextColor(mContext.getResources().getColor(R.color.night_mode_text_secondary));
			viewHolder.tips.setTextColor(mContext.getResources().getColor(R.color.night_mode_text_primary));
		}else {
			view.setBackgroundColor((mContext.getResources().getColor(android.R.color.transparent)));
			viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.day_mode_text_primary));
			viewHolder.brefing.setTextColor(mContext.getResources().getColor(R.color.day_mode_text_secondary));
			viewHolder.tips.setTextColor(mContext.getResources().getColor(R.color.day_mode_text_primary));
		}
		

		
	}

	private void loadPicture(String url, ImageView imageView) {
		Glide.with(MyApplication.getGlobalContext()).load(url)
		.diskCacheStrategy(DiskCacheStrategy.RESULT)//.override(130, 74)
		.centerCrop()//.placeholder(R.drawable.place_holder)
		.into(imageView);
	}
	
	


	@Override
	public Object getItem(int position) {
		mCursor.moveToPosition(position);
		
		return TabListItemBean.fromCursor(mCursor);
	}









	public class ViewHolder {
		public ImageView imageView;
		public TextView title;
		public TextView brefing;
		public TextView tips;

	}
	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		ViewHolder viewHolder = new ViewHolder();
		View view = LayoutInflater.from(arg0).inflate(R.layout.list_item, arg2, false);

		
		
		viewHolder.imageView = (ImageView) view
				.findViewById(R.id.imageview);
		viewHolder.title = (TextView) view.findViewById(R.id.title);
		viewHolder.brefing = (TextView) view
				.findViewById(R.id.brefing);
		viewHolder.tips = (TextView) view.findViewById(R.id.tips);
		
		
		
		view.setTag(viewHolder);
		return view;
	}
	

}
