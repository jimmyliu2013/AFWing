package com.lim.afwing.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lim.afwing.R;
import com.lim.afwing.activitys.Constant;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.beans.TabListItemBean;
import com.lim.afwing.dao.Contract;

public class HeaderViewPagerAdapter extends PagerAdapter {

	//private List<ImageView> viewList;
	//private List<HashMap<String, String>> dataList;
	private int mChildCount = 0;
	private Context mContext;
	private Cursor mCursor;

	public HeaderViewPagerAdapter(Context context, Cursor cursor) {
		this.mContext = context;
		//this.viewList = new ArrayList<ImageView>();
		//this.dataList = dataList;
		this.mCursor = cursor;
	}

	
	public void swapCursor(Cursor cursor) {
	        this.mCursor = cursor;
	}
	
	

	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		position = position % mCursor.getCount();
	    mCursor.moveToPosition(position);
//	    final ViewHolder viewHolder;
//	    
//	    if (container == null) {
//			
//		viewHolder = new ViewHolder();
//	    View view = View.inflate(mContext, R.layout.header_item, null);
//	    viewHolder.imageView = (ImageView) view.findViewById(R.id.image);
//	    viewHolder.textView = (TextView) view.findViewById(R.id.image_indicator_text);
//	    viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.image_indicator_dot_layout);
//	    
//	    container.setTag(viewHolder);
//	    }else {
//	    	viewHolder = (ViewHolder) container.getTag();
//		}
	    
	    View view = View.inflate(mContext, R.layout.header_item, null);
	    ImageView imageView = (ImageView) view.findViewById(R.id.image);
	    TextView textView = (TextView) view.findViewById(R.id.image_indicator_text);
	    LinearLayout dotContainer = (LinearLayout) view.findViewById(R.id.image_indicator_dot_layout);
	    
	    for (int i = 0; i < Constant.HEADER_ITEM_COUNT; i++) {
	  			ImageView dotView = new ImageView(mContext);
	  			LayoutParams params = new LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()), 
	  					(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()));
	  			params.leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics());
	  			params.gravity = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, mContext.getResources().getDisplayMetrics());
	  			dotView.setLayoutParams(params);
	  			
	  			dotView.setBackgroundResource(R.drawable.indicator_dot_selector);
	  			dotView.setEnabled(false);
	  			if (i == position){
	  				dotView.setEnabled(true);
				}
	  			//

	  			dotContainer.addView(dotView);

	  		}
	    
	    String imageUrl = mCursor.getString(mCursor.getColumnIndex(Contract.HeaderColumnName.IMAGE_URL));
	    String title = mCursor.getString(mCursor.getColumnIndex(Contract.HeaderColumnName.TITLE));
	    //imageView.setImageResource(R.drawable.placeholder_big);
	    
	    if (MyApplication.isWifiOnly()) {
			if (MyApplication.isWifiConnected()) {
				loadPicture(imageView, imageUrl);
			}else if (!MyApplication.isConnected()) {
				loadPicture(imageView, imageUrl);
			}
		}else{
			loadPicture(imageView, imageUrl);
		}
	    
	    
	    
//	    Glide.with(mContext).load(imageUrl)
//		.diskCacheStrategy(DiskCacheStrategy.RESULT)// .override(100,
//		//.centerCrop()
//		//.fitCenter()
//		.into(imageView);
	    
	    textView.setText(title);
	    
		container.addView(view);
	
		return view;
	}


	private void loadPicture(ImageView imageView, String imageUrl) {
		Glide.with(MyApplication.getGlobalContext()).load(imageUrl)
		.diskCacheStrategy(DiskCacheStrategy.SOURCE)//.override(130, 74)
		//.centerCrop()//.placeholder(R.drawable.place_holder)
		.into(imageView);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

	@Override
	public int getCount() {
	    if(mCursor == null) {
	        return 0;
	    } else {
	        return Constant.HEADER_ITEM_COUNT; //mCursor.getCount();
	    }
	}
	
/*	public class ViewHolder {
		public ImageView imageView;
	    public TextView textView;
	    public LinearLayout linearLayout;
	}*/
	
	
	public TabListItemBean getItem(int position){
	    
		mCursor.moveToPosition(position);
		return TabListItemBean.fromCursor(mCursor);
	}



	
}