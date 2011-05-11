package com.apps.smsbiljett;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

public class Downloader {
	
	final static Handler mHandler = new Handler();
	
	private static ImageView target;
	private static Drawable drawable;
	
	private static Main main;
	private static String text;
	
	private static boolean failed = false;
 
	public static void Image(final String url, ImageView ta)
	{
		target = ta;
		
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                downloadImage(url);
                mHandler.post(mUpdateResults);
            }
        };
        t.start();
	}
	
	private static void downloadImage(String url)
	{
		try
		{
			InputStream is = (InputStream) new URL(url).getContent();
			drawable = Drawable.createFromStream(is, "src name");
		}catch (Exception e) {
			
			System.out.println("URL="+url+" Exc="+e);
		}
	}
	
	public static void Text(final String URL, Main ma)
    {
		main = ma;
		failed = false;
		
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                downloadText(URL);
                mHandler.post(mUpdateTextResults);
            }
        };
        t.start();      
    }
	
	public static void Text(final String URL)
    {
		// Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                downloadText(URL);
            }
        };
        t.start();      
    }
	
	private static void downloadText(String url)
	{
		int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpConnection(url);
        } catch (IOException e1) {
            e1.printStackTrace();
            text = null;
            return;
        }
        
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
          String str = "";
          char[] inputBuffer = new char[BUFFER_SIZE];          
        try {
            while ((charRead = isr.read(inputBuffer))>0)
            {                    
                //---convert the chars to a String---
                String readString = 
                    String.copyValueOf(inputBuffer, 0, charRead);                    
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        text = str;
	}
	
	private static InputStream OpenHttpConnection(String urlString) 
    throws IOException
    {
        InputStream in = null;
        int response = -1;
               
        URL url = new URL(urlString); 
        URLConnection conn = url.openConnection();
                 
        if (!(conn instanceof HttpURLConnection))                     
            throw new IOException("Not an HTTP connection");
        
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); 

            response = httpConn.getResponseCode();                 
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();                                 
            }                     
        }
        catch (Exception ex)
        {
            throw new IOException("Error connecting");            
        }
        return in;     
    }
	
	private static void updateUi()
	{
		if(drawable != null && target != null)
		{
			target.setImageDrawable(drawable);
		}
	}
	
	private static void updateText()
	{
		if(text != null && main != null)
		{
			main.onRecieveBannerInfo(text);
		}
		else if(text == null)
			main.onInternetFailed();
	}
	
	// Create runnable for posting
    final static Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateUi();
        }
    };
    
 // Create runnable for posting
    final static Runnable mUpdateTextResults = new Runnable() {
        public void run() {
            updateText();
        }
    };


}
