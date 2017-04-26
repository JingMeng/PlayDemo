package com.sinieco.playdemo.play.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.sinieco.playdemo.R;
import com.sinieco.playdemo.play.IView.IVideoListView;
import com.sinieco.playdemo.play.activity.Persenter.VideoPresenterImpl;

/**
 * Created by BaiMeng on 2017/4/21.
 */
public class ViedoListActivity extends AppCompatActivity implements IVideoListView {

    private RecyclerView videolist;
    private VideoPresenterImpl videoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
        videolist = (RecyclerView) findViewById(R.id.rv_video_list);
        videoPresenter = new VideoPresenterImpl(this);
        toSetAdapter();
    }

    private void toSetAdapter() {
        videoPresenter.setAdapter(this,videolist);
    }

}
