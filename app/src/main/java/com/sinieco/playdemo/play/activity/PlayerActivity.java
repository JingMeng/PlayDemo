package com.sinieco.playdemo.play.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sinieco.playdemo.R;
import com.sinieco.playdemo.common.PlayerManager;
import com.sinieco.playdemo.media.IjkVideoView;
import com.sinieco.playdemo.play.IView.IPlayerView;
import com.sinieco.playdemo.play.Video;
import com.sinieco.playdemo.play.activity.Persenter.IPlayerPersenter;
import com.sinieco.playdemo.play.activity.Persenter.PlayerPersenterImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PlayerActivity extends AppCompatActivity implements IPlayerView {
    private PlayerManager player;
    private IPlayerPersenter playerPersenter ;
    private LinearLayout top;
    private RelativeLayout bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top = (LinearLayout) findViewById(R.id.ll_top);
        bottom = (RelativeLayout) findViewById(R.id.rl_bottom);

        playerPersenter = new PlayerPersenterImpl(this);
        playerPersenter.getVideoInfo(player ,this);
        playerPersenter.initTopAndBottom(top , bottom);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (playerPersenter.touch(event))
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player = null ;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void topAndBottomShow(LinearLayout top, RelativeLayout bottom) {

    }
}
