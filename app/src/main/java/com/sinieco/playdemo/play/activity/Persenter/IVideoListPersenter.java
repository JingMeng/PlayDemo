package com.sinieco.playdemo.play.activity.Persenter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by BaiMeng on 2017/4/24.
 */
public interface IVideoListPersenter {
    void setAdapter(Activity activity , RecyclerView videoList);
}
