package com.sinieco.playdemo.play.activity.Persenter;

import android.app.Activity;
import android.view.MotionEvent;

import com.sinieco.playdemo.common.PlayerManager;
import com.sinieco.playdemo.play.Video;

/**
 * Created by BaiMeng on 2017/4/26.
 */
public interface IPlayerPersenter {
    void getVideoInfo( PlayerManager player , Activity Activity);

    boolean touch(MotionEvent event);
}
