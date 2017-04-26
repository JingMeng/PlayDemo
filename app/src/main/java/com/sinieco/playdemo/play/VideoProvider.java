package com.sinieco.playdemo.play;

import android.Manifest;
import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

import com.sinieco.playdemo.common.XPermissionUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BaiMeng on 2017/4/21.
 */
public class VideoProvider  {
    private Activity context;

    public VideoProvider(Activity context) {
        this.context = context;
    }

    public Observable<List<Video>> getList() {
        final List<Video> list = new ArrayList<Video>();
        return Observable.create(new Observable.OnSubscribe<List<Video>>() {
            @Override
            public void call(final Subscriber<? super List<Video>> subscriber) {
                XPermissionUtils.requestPermissions(context, 1, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        if (context != null) {
                            Cursor cursor = context.getContentResolver().query(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                                    null, null);
                            if (cursor != null) {
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
                                    Video video = new Video(id, title, album, artist, displayName, mimeType, path, size, duration);
                                    list.add(video);
                                }
                                cursor.close();
                                subscriber.onNext(list);
                            }
                        }
                    }

                    @Override
                    public void onPermissionDenied() {
                        return;
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

