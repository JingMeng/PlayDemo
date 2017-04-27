package com.sinieco.playdemo.play.activity.Modle;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.Observable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sinieco.playdemo.R;
import com.sinieco.playdemo.common.PlayerManager;
import com.sinieco.playdemo.play.Video;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by BaiMeng on 2017/4/26.
 */
public class PlayerModle implements PlayerManager.PlayerStateListener{
    private Video video ;
    private PlayerManager player;
    private Activity activity;
    private GestureDetector gestureDetector ;
    private boolean isShowing = true ;
    private LinearLayout top;
    private RelativeLayout bottom ;

    @Override
    public void onComplete() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }

    public void initPlayer(){
        gestureDetector = new GestureDetector(activity,new MyGestureDetector());
        player = new PlayerManager(activity);
        player.setFullScreenOnly(true);
        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(true);
        player.setPlayerStateListener(this);
        player.play(video.getPath());
    }


    public void getVideoInfo(final PlayerManager player, Activity activity) {
        this.player = player ;
        this.activity = activity ;
        Intent mIntent = activity.getIntent();
        String action = mIntent.getAction();
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            Uri uri = mIntent.getData();
            if (TextUtils.equals(uri.getScheme(), "content")) {
                getVideo(uri).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Video>() {
                    @Override
                    public void call(Video video) {
                        initPlayer();
                    }
                });
            }
        }else {
            video = (Video)mIntent.getSerializableExtra("video");
            initPlayer();
        }
    }

    rx.Observable<Video> getVideo( final Uri uri ){
        return rx.Observable.create(new rx.Observable.OnSubscribe<Video>() {
            @Override
            public void call(Subscriber<? super Video> subscriber) {
                String[] projection={
                        MediaStore.Video.Media.DISPLAY_NAME,//视频在sd卡中的名称
                        MediaStore.Video.Media.DURATION,//视频时长
                        MediaStore.Video.Media.SIZE,//视频文件的大小
                        MediaStore.Video.Media.DATA,//视频的绝对路径
                        MediaStore.Video.Media.ARTIST,//艺术家
                        MediaStore.Video.Media._ID ,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.ALBUM,
                        MediaStore.Video.Media.MIME_TYPE
                };
                ContentResolver contentResolver= activity.getContentResolver();
                Cursor cursor= contentResolver.query(uri,projection,null,null,null);
                if(cursor != null ) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        String title = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        String album = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                        String artist = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                        String displayName = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        String mimeType = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                        String path = cursor
                                .getString(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        long duration = cursor
                                .getInt(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        long size = cursor
                                .getLong(cursor
                                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                        subscriber.onNext(video = new Video(id, title, album, artist, displayName, mimeType, path, size, duration));
                    }
                }
            }
        });

    }

    public boolean touch(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
    int height = 0  ;
    public void getTopAndBottomHeight(){
        if(height == 0){
            ViewTreeObserver vto = top.getViewTreeObserver();

            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener()
            {
                public boolean onPreDraw()
                {
                    height = top.getMeasuredHeight();
                    return true;
                }
            });

        }
        new Handler().postDelayed(new Runnable(){
            public void run() {
                topAndBottomShow();
            }
        }, 50);
    }
    public void topAndBottomShow() {

        if(isShowing){
            ObjectAnimator.ofFloat(bottom,"translationY", 0, height).setDuration(200).start();
            ObjectAnimator.ofFloat(top,"translationY", 0, -height).setDuration(200).start();
            isShowing = false ;
        }else {
            ObjectAnimator.ofFloat(bottom,"translationY", height ,0).setDuration(200).start();
            ObjectAnimator.ofFloat(top,"translationY", -height ,0).setDuration(200).start();
            isShowing = true ;
        }


    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        private boolean firstScroll  ;
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("双击","666");
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            firstScroll = true ;
            return false;
        }
//
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            float mOldX = e1.getX(), mOldY = e1.getY();
//            int y = (int) e2.getRawY();
//            if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
//                // 横向的距离变化大则调整进度，纵向的变化大则调整音量
//                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
//                    gesture_progress_layout.setVisibility(View.VISIBLE);
//                    gesture_volume_layout.setVisibility(View.GONE);
//                    gesture_bright_layout.setVisibility(View.GONE);
//                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
//                } else {
//                    if (mOldX > playerWidth * 3.0 / 5) {// 音量
//                        gesture_volume_layout.setVisibility(View.VISIBLE);
//                        gesture_bright_layout.setVisibility(View.GONE);
//                        gesture_progress_layout.setVisibility(View.GONE);
//                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
//                    } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
//                        gesture_bright_layout.setVisibility(View.VISIBLE);
//                        gesture_volume_layout.setVisibility(View.GONE);
//                        gesture_progress_layout.setVisibility(View.GONE);
//                        GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
//                    }
//                }
//            }
//
//            // 如果每次触摸屏幕后第一次scroll是调节进度，那之后的scroll事件都处理音量进度，直到离开屏幕执行下一次操作
//            if (GESTURE_FLAG == GESTURE_MODIFY_PROGRESS) {
//                // distanceX=lastScrollPositionX-currentScrollPositionX，因此为正时是快进
//                if (Math.abs(distanceX) > Math.abs(distanceY)) {// 横向移动大于纵向移动
//                    if (distanceX >= DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快退，用步长控制改变速度，可微调
//                        gesture_iv_progress.setImageResource(R.drawable.souhu_player_backward);
//                        if (playingTime > 3) {// 避免为负
//                            playingTime -= 3;// scroll方法执行一次快退3秒
//                        } else {
//                            playingTime = 0;
//                        }
//                    } else if (distanceX <= -DensityUtil.dip2px(this, STEP_PROGRESS)) {// 快进
//                        gesture_iv_progress.setImageResource(R.drawable.souhu_player_forward);
//                        if (playingTime < videoTotalTime - 16) {// 避免超过总时长
//                            playingTime += 3;// scroll执行一次快进3秒
//                        } else {
//                            playingTime = videoTotalTime - 10;
//                        }
//                    }
//                    if (playingTime < 0) {
//                        playingTime = 0;
//                    }
//                    tv_pro_play.seekTo(playingTime);
//                    geture_tv_progress_time.setText(DateTools.getTimeStr(playingTime) + "/" + DateTools.getTimeStr(videoTotalTime));
//                }
//            }
//
//            // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
//            else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
//                currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
//                if (Math.abs(distanceY) > Math.abs(distanceX)) {// 纵向移动大于横向移动
//                    if (distanceY >= DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调大,注意横屏时的坐标体系,尽管左上角是原点，但横向向上滑动时distanceY为正
//                        if (currentVolume < maxVolume) {// 为避免调节过快，distanceY应大于一个设定值
//                            currentVolume++;
//                        }
//                        gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_volume);
//                    } else if (distanceY <= -DensityUtil.dip2px(this, STEP_VOLUME)) {// 音量调小
//                        if (currentVolume > 0) {
//                            currentVolume--;
//                            if (currentVolume == 0) {// 静音，设定静音独有的图片
//                                gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_silence);
//                            }
//                        }
//                    }
//                    int percentage = (currentVolume * 100) / maxVolume;
//                    geture_tv_volume_percentage.setText(percentage + "%");
//                    audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,currentVolume, 0);
//                }
//            }
//
//            // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
//            else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
//                gesture_iv_player_bright.setImageResource(R.drawable.souhu_player_bright);
//                if (mBrightness < 0) {
//                    mBrightness = getWindow().getAttributes().screenBrightness;
//                    if (mBrightness <= 0.00f)
//                        mBrightness = 0.50f;
//                    if (mBrightness < 0.01f)
//                        mBrightness = 0.01f;
//                }
//                WindowManager.LayoutParams lpa = getWindow().getAttributes();
//                lpa.screenBrightness = mBrightness + (mOldY - y) / playerHeight;
//                if (lpa.screenBrightness > 1.0f)
//                    lpa.screenBrightness = 1.0f;
//                else if (lpa.screenBrightness < 0.01f)
//                    lpa.screenBrightness = 0.01f;
//                getWindow().setAttributes(lpa);
//                geture_tv_bright_percentage.setText((int) (lpa.screenBrightness * 100) + "%");
//            }
//
//            firstScroll = false;// 第一次scroll执行完成，修改标志
//            return false;
//        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
           if(player.isPlaying()){
               player.pause();
           }else {
               player.start();
           }
            if(height == 0){
                getTopAndBottomHeight();
            }else {
                topAndBottomShow();
            }
            return true;
        }
    }

    public void initTopAndBottom(final LinearLayout top, final RelativeLayout bottom){
        this.top = top ;
        this.bottom = bottom ;
        getTopAndBottomHeight();
    }

}
