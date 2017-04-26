package com.sinieco.playdemo.play.activity.Persenter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.sinieco.playdemo.play.IView.IVideoListView;
import com.sinieco.playdemo.play.activity.Modle.VideoViewModle;

/**
 * Created by BaiMeng on 2017/4/24.
 */
public class VideoPresenterImpl implements IVideoListPersenter {
    private IVideoListView videoListView ;
    private VideoViewModle modle ;
    public VideoPresenterImpl(IVideoListView videoListView) {
        this.videoListView = videoListView ;
        modle = new VideoViewModle();
    }

    @Override
    public void setAdapter(Activity activity , RecyclerView videoList) {
        modle.setAdapter(activity ,videoList);
    }
}
