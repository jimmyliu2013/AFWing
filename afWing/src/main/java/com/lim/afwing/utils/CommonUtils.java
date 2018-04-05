package com.lim.afwing.utils;

import java.io.File;

import android.app.Activity;
import android.util.Log;

import com.lim.afwing.R;

public class CommonUtils {

public static void setActivityBackground(Activity context, boolean isNightMode) {
		
		if (isNightMode) {
			context.getWindow().getDecorView().setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
		} else {
			context.getWindow().getDecorView().setBackgroundColor(context.getResources().getColor(R.color.day_mode_background));
		}
	}
	
public static void deleteFilesByDirectory(File directory) {
	if (directory != null && directory.exists() && directory.isDirectory()) {
		for (File item : directory.listFiles()) {
			try {
				item.delete();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e("AFWing", "Failed to delete file: " + item.getAbsolutePath());
			}
			
		}
	}
}

}
