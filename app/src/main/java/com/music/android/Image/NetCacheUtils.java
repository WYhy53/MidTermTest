package com.music.android.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetCacheUtils {
    public static final int SUCESS=1;
    public static final int FAIL=2;
    private final LocalCacheUtils localCacheUtils;
    private final MemoryCacheUtils memoryCacheUtils;
    private ExecutorService service;
    public NetCacheUtils(LocalCacheUtils localCacheUtils,MemoryCacheUtils memoryCacheUtils){
        service= Executors.newFixedThreadPool(10);
        this.localCacheUtils=localCacheUtils;
        this.memoryCacheUtils=memoryCacheUtils;
    }
    //联网请求得到图片
    public void getBitmapFromNet(String imageUrl){
        service.execute(new MyRunnable(imageUrl));
    }
    class MyRunnable implements Runnable{
        private final String imageUrl;
        public MyRunnable(String imageUrl){
            this.imageUrl=imageUrl;
            MyRunnable myThread=new MyRunnable(imageUrl);
            new Thread(myThread).start();
        }
        @Override
        public void run(){
            //子线程，请求网络图片
            try {
                URL url=new URL(imageUrl);
                HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.connect();
                int code=connection.getResponseCode();
                if (code==200){
                    InputStream is=connection.getInputStream();
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    //显示到控件上，发消息把Bitmap发出去和position
                    Message msg=Message.obtain();
                    msg.what=SUCESS;
                    msg.obj=bitmap;
                    //在内存中缓存一份
                    memoryCacheUtils.putBitmap(imageUrl,bitmap);
                    //在本地中缓存一份
                    localCacheUtils.putBitmap(imageUrl,bitmap);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
