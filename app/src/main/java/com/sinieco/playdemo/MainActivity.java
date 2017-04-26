package com.sinieco.playdemo;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.Observable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.sinieco.playdemo.common.PlayerManager;
import com.sinieco.playdemo.media.IjkVideoView;
import com.sinieco.playdemo.play.Video;
import com.sinieco.playdemo.utils.UriUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements PlayerManager.PlayerStateListener {
//    private String url1 = "rtmp://203.207.99.19:1935/live/CCTV5";
//    private String url2 = "http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8";
//    private String url3 = "rtsp://184.72.239.149/vod/mp4:BigBuckBunny_115k.mov";
//    private String url4 = "http://42.96.249.166/live/24035.m3u8";
    private PlayerManager player;
    private IjkVideoView videoView;
    private Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mIntent = getIntent();
        String action = mIntent.getAction();
        videoView = (IjkVideoView)findViewById(R.id.video_view);
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            Uri uri = mIntent.getData();
            if (TextUtils.equals(uri.getScheme(), "content")) {
                getVideo(uri).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Video>() {
                    @Override
                    public void call(Video video) {
                        initPlayer(video);
                    }
                });
            }
        }else {
            video = (Video)mIntent.getSerializableExtra("video");
            initPlayer(video);
        }
    }

    private void initPlayer(Video video) {
        if(this.video == null){
            this.video = video ;
        }
        player = new PlayerManager(this);
        player.setFullScreenOnly(true);
        player.setScaleType(PlayerManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(true);
        player.setPlayerStateListener(this);
        player.play(video.getPath());
        Log.i("Video ++++++++++++++++",video.toString());

//        videoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
//        videoView.setVideoURI(Uri.parse("http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8"));
//        videoView.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (player.gestureDetector.onTouchEvent(event))

            return true;
        return super.onTouchEvent(event);
    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video = null ;
        player = null ;
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
                ContentResolver contentResolver= MainActivity.this.getContentResolver();
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
}
