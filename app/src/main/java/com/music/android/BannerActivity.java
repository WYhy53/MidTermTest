package com.music.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.music.android.Image.BitmapCacheUtils;
import com.music.android.gson.Albums;
import com.music.android.gson.Banner;
//import com.music.android.util.HttpUtils;
import com.music.android.util.Utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BannerActivity extends AppCompatActivity {
    private ScrollView bannerlayout;
    private LinearLayout albumlayout;
    private RelativeLayout item1layout;
    private RelativeLayout item2layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        bannerlayout = (ScrollView) findViewById(R.id.banner_layout);
        albumlayout = (LinearLayout) findViewById(R.id.album_layout);
        item1layout = (RelativeLayout) findViewById(R.id.item_1);
        item2layout = (RelativeLayout) findViewById(R.id.item_2);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String albumString = prefs.getString("album1", null);
        if (albumString != null) {
            Albums albums = Utility.handleAlbumResponse(albumString);
            if (albums != null) {
                Log.d("已经获取到了信息", albumString);
                showAlbum1(albums);
                showAlbum2(albums);
            } else {
                Log.d("jjjjjjjjjjjjjjjjjj", "album is null");
            }
        } else {
            Log.d("wwwwwwwwwwwwwwwwwwwwwww", "albumstring is null");
//具体网络请求
            final Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 1:
                            String text = (String) msg.obj;
                            Log.d("this", "执行请求操作");
                            requestalbum(text);
                            break;
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://47.99.165.194/album/newest");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        int code = connection.getResponseCode();
                        if (code == 200) {
                            InputStream inputStream = connection.getInputStream();
                            InputStreamReader streamReader = new InputStreamReader(inputStream);
                            BufferedReader reader = new BufferedReader(streamReader);
                            StringBuilder builder = new StringBuilder();
                            String s = "";
                            while ((s = reader.readLine()) != null) {
                                builder.append(s);
                            }
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = builder.toString();
                            handler.sendMessage(msg);
                            streamReader.close();
                            reader.close();
                            Log.d("hanlder", "到了handler这里");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Button button = (Button) findViewById(R.id.account);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BannerActivity.this, PersonActivity.class);
                startActivity(intent);
            }
        });
    }

    private void requestalbum(String albumText) {
        Log.d("执行到了请求操作这里", "在这里请求");
        final Albums albums = Utility.handleAlbumResponse(albumText);
        if (albums != null) {
            SharedPreferences.Editor editor = PreferenceManager.
                    getDefaultSharedPreferences(BannerActivity.this).edit();
            editor.putString("album2", albumText);
            editor.apply();
            showAlbum1(albums);
            showAlbum2(albums);
        }
    }


    private void showAlbum1(Albums albums) {
        Log.d("showAlbum1", "这里有这里有！");
        albumlayout.removeAllViews();
        for (Banner banner : albums.albumList) {
            View view = LayoutInflater.from(this).inflate(R.layout.album_iteme1, item1layout, false);
            ImageView picImage1 = (ImageView) view.findViewById(R.id.pic1);
            TextView songnameText1 = (TextView) view.findViewById(R.id.songname1);
            TextView artistText1 = (TextView) view.findViewById(R.id.artist1);
            songnameText1.setText(banner.name);
            artistText1.setText(banner.artist.name);
            String imageUrl1 = banner.picUrl;
            Log.d("拉阿拉啦啦啦啦拉拉阿拉", banner.name);
            BitmapCacheUtils bitmapCacheUtils = new BitmapCacheUtils(imageUrl1);
            Bitmap bitmap1 = bitmapCacheUtils.getBitmap();
            picImage1.setImageBitmap(bitmap1);
            item1layout.addView(view);
        }
    }

    private void showAlbum2(Albums albums) {
        Log.d("showAlbum2", "这里也有这里也有！");
        albumlayout.removeAllViews();
        for (Banner banner : albums.albumList) {
            View view = LayoutInflater.from(this).inflate(R.layout.album_item2, item2layout, false);
            ImageView picImage2 = (ImageView) view.findViewById(R.id.pic2);
            TextView songnameText2 = (TextView) view.findViewById(R.id.songname2);
            TextView artistText2 = (TextView) view.findViewById(R.id.artist2);
            songnameText2.setText(banner.name);
            artistText2.setText(banner.artist.name);
            String imageUrl2 = banner.picUrl;
            Log.d("wooooooooo我我我我我", banner.artist.name);
            BitmapCacheUtils bitmapCacheUtils = new BitmapCacheUtils(imageUrl2);
            Bitmap bitmap = bitmapCacheUtils.getBitmap();
            picImage2.setImageBitmap(bitmap);
            item2layout.addView(view);
        }
    }
}
