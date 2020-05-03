package com.music.android.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class LocalCacheUtils {
    private final MemoryCacheUtils memoryCacheUtils;
    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils){
        this.memoryCacheUtils=memoryCacheUtils;
    }
    //根据Url获取图片
    public Bitmap getBitmapFromUrl(String imageUrl){
        //判断sdcard是否挂载
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                String fileName=MD5Encoder.encode(imageUrl);
                File file=new File(Environment.getExternalStorageDirectory()+"/news",fileName);
                if (file.exists()){
                    FileInputStream is=new FileInputStream(file);
                    Bitmap bitmap= BitmapFactory.decodeStream(is);
                    if (bitmap!=null){
                        memoryCacheUtils.putBitmap(imageUrl,bitmap);
                        Log.d("this","把本地保持到内存中");
                    }
                    return bitmap;
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.d("this","获取图片失败");
            }
        }
        return null;
    }
    //根据Url保存图片
    public void putBitmap(String imageUrl,Bitmap bitmap){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            try {
                String fileName=MD5Encoder.encode(imageUrl);
                File file=new File(Environment.getExternalStorageDirectory()+"/news",fileName);
                File parentFile=file.getParentFile();
                if (!parentFile.exists()){
                    //创建目录
                    parentFile.mkdirs();
                }
                if (!file.exists()){
                    file.createNewFile();
                }
                //保存图片
                bitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
            }catch (Exception e){
                e.printStackTrace();
                Log.d("this","图片本地缓存失败");
            }
        }
    }
}
