package com.lim.afwing.fragments;

import java.io.IOException;

import com.lim.afwing.R;
import com.lim.afwing.activitys.Constant;
import com.lim.afwing.applications.MyApplication;
import com.lim.afwing.utils.HtmlParser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Toast;

public class WebFragment extends Fragment {

	private int mPageNumber;
	private int mTotalPageNumber;
	private String mUrl;
	private Context mContext;
	private WebView webView;
	private View loadingView;
	private View emptyView;
	private String html;

	public static WebFragment newInstance(String url, int pageNumber,
			int totalPageNumber) {
		WebFragment f = new WebFragment();
		Bundle args = new Bundle();
		args.putInt("pageNumber", pageNumber);
		args.putString("url", url);
		args.putInt("totalPageNumber", totalPageNumber);
		f.setArguments(args);

		return f;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt("pageNumber");
		mUrl = getArguments().getString("url");
		mTotalPageNumber = getArguments().getInt("totalPageNumber");
		mContext = getActivity();

	}

	public void refreshWebView() {

		if (webView != null) {
		} else {
			if (mUrl != null && mPageNumber != 0) {

			}
		}

	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_web, container,
				false);
		webView = (WebView) rootView.findViewById(R.id.wv_fragment_web);

		loadingView = rootView.findViewById(R.id.ll_loading);
		emptyView = rootView.findViewById(R.id.ll_empty);

		emptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new LoadHtmlAsyncTask().execute(jointUrl(mUrl, mPageNumber));
			}
		});
		
		
		if (MyApplication.isNightMode()) {
			
			loadingView.setBackgroundColor(Color.parseColor("#404040"));
			emptyView.setBackgroundColor(Color.parseColor("#404040"));
			webView.setBackgroundColor(Color.parseColor("#404040"));
		}
		

		WebSettings settings = webView.getSettings();

		switch (MyApplication.getFontSize()) {
		case "1":
			settings.setTextSize(TextSize.LARGEST);
			break;
		case "2":
			settings.setTextSize(TextSize.LARGER);
			break;
		case "3":
			settings.setTextSize(TextSize.NORMAL);
			break;
		case "4":
			settings.setTextSize(TextSize.SMALLER);
			break;
		case "5":
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			settings.setTextSize(TextSize.NORMAL);
			break;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			settings.setLayoutAlgorithm(LayoutAlgorithm.TEXT_AUTOSIZING);
		} else {
			settings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		}

		if (Build.VERSION.SDK_INT >= 19) {
			settings.setLoadsImagesAutomatically(true);
		} else {
			settings.setLoadsImagesAutomatically(false);
		}

		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {

				if (!webView.getSettings().getLoadsImagesAutomatically()) {
					webView.getSettings().setLoadsImagesAutomatically(true);
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
			}

		});

		new LoadHtmlAsyncTask().execute(jointUrl(mUrl, mPageNumber));

		return rootView;
	}

	private String jointUrl(String url, int page) {
		String subUrl = url.substring(0, url.lastIndexOf("."));
		if (page != 1) {
			url = subUrl + "_" + page + ".html";
		}
		return url;

	}


	private class LoadHtmlAsyncTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected void onPreExecute() {
			loadingView.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
			emptyView.setVisibility(View.GONE);
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				boolean loadImage = true;
				
				if (MyApplication.isWifiOnly() && (!MyApplication.isWifiConnected())) {
					loadImage = false;
				}
				
				
				String tempHtml = HtmlParser.getHtmlFromWeb(params[0], MyApplication.isNightMode(), loadImage);
				if (tempHtml != null && tempHtml != "") {
					html = tempHtml;
					return Constant.SUCCESS;
				} else {
					return Constant.END;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.FAIL;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {

			switch (result) {
			case Constant.SUCCESS:

				webView.setVisibility(View.VISIBLE);
				emptyView.setVisibility(View.GONE);
				loadingView.setVisibility(View.GONE);

				/*
				 * html =
				 * "<html><head><meta http-equiv=\"Content-Language\" content=\"zh-cn\">"
				 * +
				 * "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
				 * + html + "</body></html>";
				 */

				//html = "<html><head><style>img{max-width: 100%; width:auto; height: auto;}</style><meta http-equiv=\"Content-Language\" content=\"zh-cn\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></head><body>"
				//		+ html + "</body></html>";

				webView.loadDataWithBaseURL(null, html, "text/html", "utf-8",
						null);

				break;

			case Constant.END:

				webView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				Toast.makeText(mContext, getResources().getString(R.string.search_end_toast_text), 0).show();

				break;

			case Constant.FAIL:

				webView.setVisibility(View.GONE);
				emptyView.setVisibility(View.VISIBLE);
				loadingView.setVisibility(View.GONE);
				break;

			default:
				break;
			}

		}

	}

}
