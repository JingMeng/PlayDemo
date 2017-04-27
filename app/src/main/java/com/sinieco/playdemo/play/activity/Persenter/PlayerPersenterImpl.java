package com.sinieco.playdemo.play.activity.Persenter;

import android.app.Activity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sinieco.playdemo.common.PlayerManager;
import com.sinieco.playdemo.play.IView.IPlayerView;
import com.sinieco.playdemo.play.Video;
import com.sinieco.playdemo.play.activity.Modle.PlayerModle;

/**
 * Created by BaiMeng on 2017/4/26.
 */
public class PlayerPersenterImpl implements IPlayerPersenter {
    IPlayerView playerView ;
    PlayerModle modle  ;
    public PlayerPersenterImpl(IPlayerView playerView) {
        this.playerView = playerView ;
        this.modle = new PlayerModle() ;
    }


    @Override
    public void getVideoInfo( PlayerManager player, Activity Activity) {
        modle.getVideoInfo( player , Activity);
    }

    @Override
    public boolean touch(MotionEvent event) {
        return modle.touch(event);
    }

    @Override
    public void initTopAndBottom(LinearLayout top, RelativeLayout bottom) {
        modle.initTopAndBottom(top , bottom );
    }
}
