package com.music.android.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Albums {
    @SerializedName("albums")
    public List<Banner> albumList;
}
