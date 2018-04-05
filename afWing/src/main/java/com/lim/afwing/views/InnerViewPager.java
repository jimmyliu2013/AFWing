package com.lim.afwing.views;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InnerViewPager extends ViewPager {

	
	private int downX;
	private int downY;
	
	
	public InnerViewPager(Context context) {
		super(context);
	}

	public InnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	PointF downPoint = new PointF();
	OnSingleTouchListener onSingleTouchListener;

	@Override
	public boolean dispatchTouchEvent(MotionEvent evt) {
		switch (evt.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ��¼����ʱ������
			downX = (int)evt.getX();
			downY = (int)evt.getY();
			if (this.getChildCount() > 1) { //�����ݣ�����1��ʱ
				// ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������
				
				
				
				
				
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) evt.getX();
			int moveY = (int) evt.getY();
			
			int diffX = (int) (downX - moveX);
			int diffY = (int) (downY - moveY);
			
			
			if(Math.abs(diffX) > Math.abs(diffY)){
		/*	if (this.getChildCount() > 1) { //�����ݣ�����1��ʱ
				// ֪ͨ�丸�ؼ������ڽ��е��Ǳ��ؼ��Ĳ���������������
				getParent().requestDisallowInterceptTouchEvent(true);
			}else*/ 
				if(getCurrentItem() == (getAdapter().getCount() -1)
					&& diffX > 0) {
				// ��ǰҳ��������һ��, �����Ǵ������󻬶�, ��������
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			}else {
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			
			
			
			
			
			
			break;
		case MotionEvent.ACTION_UP:
			// ��upʱ�ж��Ƿ��º����ֵ����Ϊһ����
			if (PointF.length(evt.getX() - downX, evt.getY()
					- downY) < (float) 5.0) {
				onSingleTouch(this);
				return true;
			}
			break;
		}
			
			
	
		return super.dispatchTouchEvent(evt);
	}

	public void onSingleTouch(View v) {
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch(v);
		}
	}

	public interface OnSingleTouchListener {
		public void onSingleTouch(View v);
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}
}
