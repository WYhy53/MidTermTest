package com.music.android.Image;

import android.graphics.Bitmap;
import android.util.Log;

public class BitmapCacheUtils {
    final  String imageUrl;
    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;
    public BitmapCacheUtils(String url){
        this.imageUrl=url;
        memoryCacheUtils=new MemoryCacheUtils();
        localCacheUtils=new LocalCacheUtils(memoryCacheUtils);
        netCacheUtils=new NetCacheUtils(localCacheUtils,memoryCacheUtils);
    }
    public Bitmap getBitmap(){
        if (memoryCacheUtils!=null){
            Bitmap bitmap=memoryCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap!=null){
                Log.d("this","内存加载图片成功==");
                return bitmap;
            }
        }
        if (localCacheUtils!=null){
            Bitmap bitmap=localCacheUtils.getBitmapFromUrl(imageUrl);
            if (bitmap!=null){
                Log.d("this","本地加载图片成功==");
                return bitmap;
            }
        }
        //请求网络图片
        netCacheUtils.getBitmapFromNet(imageUrl);
        return null;
    }
}