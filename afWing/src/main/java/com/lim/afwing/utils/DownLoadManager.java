package com.lim.afwing.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.util.Log;

public class DownLoadManager{

public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{  
    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
        URL url = new URL(path);  
        HttpURLConnection conn =  (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5000);  
        pd.setMax(conn.getContentLength());  
        InputStream is = conn.getInputStream();  
        File file = null;
        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) {
        	file = new File(Environment.getExternalStorageDirectory()+"/apk/", "AFWing.apk");  
        	File dir = new File(Environment.getExternalStorageDirectory()+"/apk/");
			if (!dir.exists()){
				dir.mkdir();
			}
		}else {
			return null;
		}
        
				if(file.exists()){
					file.delete();
	            }
        
        
        FileOutputStream fos = new FileOutputStream(file);  
        BufferedInputStream bis = new BufferedInputStream(is);  
        byte[] buffer = new byte[1024];  
        int len ;  
        int total=0;  
        while((len =bis.read(buffer))!=-1){  
            fos.write(buffer, 0, len);  
            total+= len;  
            pd.setProgress(total);  
        }  
        fos.close();  
        bis.close();  
        is.close();  
        return file;  
    }  
    else{  
        return null;  
    }  
}  


}