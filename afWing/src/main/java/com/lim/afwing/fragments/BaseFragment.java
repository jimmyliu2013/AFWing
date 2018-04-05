package com.lim.afwing.fragments;

import com.lim.afwing.interfaces.OnSettingsChangeListener;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;


public class BaseFragment extends Fragment implements
LoaderManager.LoaderCallbacks<Cursor>, OnSettingsChangeListener{

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextSizeChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDayOrNightModeChange() {
		// TODO Auto-generated method stub
		
	}



}
