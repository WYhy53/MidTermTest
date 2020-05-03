package com.music.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.music.android.gson.Profile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class PersonActivity extends AppCompatActivity {
    private TextView textView1;
    private TextView textView2;
    private LinearLayout personDetailLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person);
        personDetailLayout=(LinearLayout)findViewById(R.id.personDetail);
        textView1=(TextView)findViewById(R.id.user_name);
        textView2=(TextView)findViewById(R.id.user_place);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String profileString=prefs.getString("profile",null);
        if (profileString != null) {
            Gson gson=new Gson();
            Profile profile=gson.fromJson(profileString,Profile.class);
            if (profile != null) {
                Log.d("已经获取到了信息", profileString);
                showProfile(profile);
            } else {
                Log.d("jjjjjjjjjjjjjjjjjj", "profile is null");
            }
        } else {
            personDetailLayout.setVisibility(View.INVISIBLE);
            Log.d("wwwwwwwwwwwwwwwwwwwwwww", "profileString is null");
//具体网络请求
            final Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 1:
                            String text = (String) msg.obj;
                            Log.d("this",text);
                            requestprofile(text);
                            break;
                    }
                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://47.99.165.194/user/detail?uid=318082831");
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
    }
    private void requestprofile(String profileText) {
        Log.d("执行到了请求操作这里","在这里请求");
        Gson gson=new Gson();
        Profile profile=gson.fromJson(profileText,Profile.class);
        if (profile!= null) {
            SharedPreferences.Editor editor = PreferenceManager.
                    getDefaultSharedPreferences(PersonActivity.this).edit();
            editor.putString("profile", profileText);
            editor.apply();
            showProfile(profile);
        }else {
            Log.d("不知道为什么","profile为NULL");
        }
    }
    private void showProfile(Profile profiles ) {
          String username=profiles.profile.nickname;
          String userplace=profiles.profile.city;
          textView1.setText(username);
          textView2.setText(userplace);
          personDetailLayout.setVisibility(View.VISIBLE);
    }
}
