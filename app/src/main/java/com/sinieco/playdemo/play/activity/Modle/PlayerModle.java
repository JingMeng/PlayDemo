package com.sinieco.playdemo.play.activity.Modle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;

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
    private MyGestureDetector gestureDetector ;

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
        return player.gestureDetector.onTouchEvent(event);
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
    }
}
