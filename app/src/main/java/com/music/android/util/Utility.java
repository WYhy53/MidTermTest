package com.music.android.util;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.music.android.Image.BitmapCacheUtils;
import com.music.android.R;
import com.music.android.gson.Albums;
import com.music.android.gson.Banner;
import com.music.android.gson.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {
    public static Albums handleAlbumResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("albums");
            String albumContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(albumContent, Albums.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
//    public static Profile handleProfilerResponse(String response){
//        try {
//            JSONObject jsonObject=new JSONObject(response);
//            JSONArray jsonArray=jsonObject.getJSONArray("profile");
//            String weatherContent=jsonArray.getJSONObject(0).toString();
//            return new Gson().fromJson(weatherContent,Profile.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
}
