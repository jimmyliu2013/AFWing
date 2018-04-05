package com.lim.afwing.adapter;

import java.util.List;
import java.util.Map;

import com.lim.afwing.R;
import com.lim.afwing.applications.MyApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchResultListAdapter extends SimpleAdapter{

	private Context mContext;

	public SearchResultListAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		TextView textView = (TextView) view.findViewById(R.id.text);
		if (textView == null) {
			throw new RuntimeException("you must have a text view whose id is 'text'!");
			
		}
		if(MyApplication.isNightMode()){
			textView.setTextColor(mContext.getResources().getColor(R.color.night_mode_text_primary));
			
		}
		
		return view;
	}
	

	

}
