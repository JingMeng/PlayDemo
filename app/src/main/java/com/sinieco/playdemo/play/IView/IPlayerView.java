package com.sinieco.playdemo.play.IView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by BaiMeng on 2017/4/26.
 */
public interface IPlayerView {
    void play();
    void pause();
    void topAndBottomShow(LinearLayout top , RelativeLayout bottom);
}
