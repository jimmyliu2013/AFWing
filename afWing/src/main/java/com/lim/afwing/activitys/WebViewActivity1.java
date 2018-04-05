package com.lim.afwing.activitys;

import java.io.IOException;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.lim.afwing.R;
import com.lim.afwing.beans.TabListItemBean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

/*public class WebViewActivity1 extends Activity {
	
	private ProgressDialog dialog;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_webview1);
		
		
		
		
		
		
		
		
		
		dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMessage("页面加载中...");
        dialog.setCancelable(true);
        dialog.setMax(100);
        dialog.show();

        final String js ="javascript: ("
                +"function () { "

                +"var css = 'html {-webkit-filter: invert(100%);' +"
                +"    '-moz-filter: invert(100%);' + "
                +"    '-o-filter: invert(100%);' + "
                +"    '-ms-filter: invert(100%); }',"

                +"head = document.getElementsByTagName('head')[0],"
                +"style = document.createElement('style');"



                //injecting the css to the head
                +"head.appendChild(style);"
                +"}());";
		
		
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String url = extras.getString("linkUrl");
		
		//getTextFromWeb(url);
		
		
		String textString = null;
		try {
			textString = getTextFromWeb(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("............."+url);
		System.out.println(textString);
		final WebView webView = (WebView) findViewById(R.id.webview);
		
		//webView.loadData(textString, "text/html", "utf-8");	
		//webView.loadDataWithBaseURL(null, textString, "text/html", "utf-8", null);
	
		ImageButton returnButton = (ImageButton) findViewById(R.id.returnbutton);
		
		
		returnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WebViewActivity.this.finish();
				
			}
		});
		
		
		if(Build.VERSION.SDK_INT >= 19) {
	        webView.getSettings().setLoadsImagesAutomatically(true);
	    } else {
	        webView.getSettings().setLoadsImagesAutomatically(false);
	    }
		//final WebView webview = (WebView)findViewById(R.id.browser);  
		// JavaScript must be enabled if you want it to work, obviously   
		//webView.setBackgroundColor(Color.parseColor("#000000"));
		
		webView.getSettings().setJavaScriptEnabled(true);  
		webView.getSettings().setDomStorageEnabled(true);  
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  
		
//webView.addJavascriptInterface(new JavaScriptInterface(), "HTMLOUT");
		// WebViewClient must be set BEFORE calling loadUrl!   
		//webView.loadData("","text/html","UTF-8");
		
		 webView.setWebChromeClient(new WebChromeClient() {
			   public void onProgressChanged(WebView view, int progress) {
			     // Activities and WebViews measure progress with different scales.
			     // The progress meter will automatically disappear when we reach 100%
				   WebViewActivity.this.setTitle("加载中...");
				   WebViewActivity.this.setProgress(progress * 100);
				   dialog.setProgress(progress );  
				   
				   if(progress == 100)
                   {
					   WebViewActivity.this.setTitle("完成");
					   dialog.dismiss();
                   }
				   
			    // WebViewActivity.this.setProgress(progress * 1000);
			   }
			 });
		
		
		webView.setWebViewClient(new WebViewClient() {  
		    @Override  
		    
		    public void onPageFinished(WebView view, String url)  
		    {  
		    	if(!webView.getSettings().getLoadsImagesAutomatically()) {
		            webView.getSettings().setLoadsImagesAutomatically(true);
		        }
		   	
		    	
			    webView.loadUrl("javascript:(function() { " +
			            "document.getElementsByClassName('top')[0].style.display='none';"+
			    		"document.getElementsByClassName('nav')[0].style.display='none';" +
			    		"document.getElementsByClassName('toppic_03')[0].style.display='none';" +
			    		"document.getElementsByClassName('footer')[0].style.display='none';" +
			    		"document.getElementsByClassName('backline')[0].style.display='none';" +
			    		"document.body.background= 'black';"+
			            "})()");
		    
			    //webView.loadUrl(js);
		       //         "})()");  
		        System.out.println("page finished!");
		        webView.setVisibility(View.VISIBLE);
		//        dialog.dismiss();
		    }  
		
		  
		 
		
		
		
		//webView.loadUrl(url);

		

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				
				
				return true;
			}



			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
	//			dialog=ProgressDialog.show(WebViewActivity.this,"���ڼ���...","�����ĵȴ�",true);

				
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub 
				Toast.makeText(WebViewActivity.this, "加载错误！ " + description, Toast.LENGTH_SHORT).show();
				WebViewActivity.this.finish();
				//super.onReceivedError(view, errorCode, description, failingUrl);
			}
			
			
			
			
			
			
			
		});
		
		
		webView.loadUrl(url);
		
		//webView.loadDataWithBaseURL(null, textString, "text/html", "utf-8", null);
		
	//	webView.loadUrl(url); 
		
		try {
	        Class clsWebSettingsClassic = 
	            getClassLoader().loadClass("android.webkit.WebSettingsClassic");
	        Method md = clsWebSettingsClassic.getMethod(
	                "setProperty", String.class, String.class);
	        md.invoke(webView.getSettings(), "inverted", "true");
	        md.invoke(webView.getSettings(), "inverted_contrast", "1");
	    } catch (Exception e) {}
	}
		
	


private String getTextFromWeb(String url) throws IOException{
	
	System.out.println("get text from web!");
	
	Document doc = Jsoup.connect(url)
			.header("User-Agent","Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2")
			.get();
	Elements textEle = doc.getElementsByClass("text");
	String text = textEle.html();
	
	
	StringBuilder sb = new StringBuilder(text.length()+100);
    char[] dataChars = text.toCharArray();

    for(int i=0;i<dataChars.length;i++){
        char ch = text.charAt(i);
        switch(ch){
        case '%':
            sb.append("%25");
            break;
        case '\'':
            sb.append("%27");
            break;
        case '#':
            sb.append("%23");
            break;
        default:
            sb.append(ch);
            break;
        }
    }
    
    
    String prefix = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
"<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
"<head>"+
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>" +
"<title>谈谈运-30 - 空军之翼</title>"+
"<meta content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no\" name=\"viewport\" id=\"viewport\" />"+
"<link href=\"http://m.afwing.com//skin/default/css/css.css\" rel=\"stylesheet\" media=\"screen\" type=\"text/css\" />"+
"</head>"+
"<body>";

    String surfix ="</body>"+
    "</html>";
    
    

    return prefix + sb.toString() + surfix;
	
	
	
	
	//System.out.println(text);
	

	//return text;
	}

	
	




	class JavaScriptInterface{
		@JavascriptInterface
		public void showHTML(String html) {
			//m_nHTMLSize = 0;
			if(html != null){
				//int i = html.lastIndexOf(""); //search for something in the text
				//m_nHTMLSize = html.length();
				Log.d("javascript test", "HTML content is: "+ html);
		 
			}else{
				Log.d("javascript test", "HTML content is: null");
			}
		}
		}
	
	
}*/
	
	
	
	

