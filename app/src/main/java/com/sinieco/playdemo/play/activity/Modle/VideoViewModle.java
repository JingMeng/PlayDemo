package com.sinieco.playdemo.play.activity.Modle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinieco.playdemo.play.activity.PlayerActivity;
import com.sinieco.playdemo.R;
import com.sinieco.playdemo.play.Video;
import com.sinieco.playdemo.play.VideoProvider;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by BaiMeng on 2017/4/24.
 */
public class VideoViewModle {
    private Activity activity ;
    public void setAdapter(final Activity activity ,final RecyclerView videoList) {
        this.activity = activity ;
        new VideoProvider(activity).getList().subscribe(new Action1<List<Video>>() {
            @Override
            public void call(List<Video> list) {
                videoList.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false));
                videoList.setAdapter(new VideoAdapter(list));
            }
        });
    }

    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
        private List <Video> list ;
        public VideoAdapter(List<Video> list) {
            this.list = list ;
        }

        @Override
        public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(activity).inflate(R.layout.item_video, null);
            return new VideoHolder(item);
        }

        @Override
        public void onBindViewHolder(VideoHolder holder, int position) {
            final Video video = list.get(position);
            holder.icon.setImageBitmap(getVideoThumbnail(video.getPath()));
            holder.title.setText(video.getTitle());
            holder.duration.setText(video.getDuration()+"");
            holder.size.setText(video.getSize()+"");
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity , PlayerActivity.class);
                    intent.putExtra("video",video);
                    activity.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class VideoHolder extends RecyclerView.ViewHolder {

            private ImageView icon ;
            private TextView title ;
            private TextView duration ;
            private TextView size ;

            public VideoHolder(View itemView) {
                super(itemView);
                icon =  (ImageView)itemView.findViewById(R.id.icon);
                title = (TextView) itemView.findViewById(R.id.title);
                duration = (TextView) itemView.findViewById(R.id.duration);
                size = (TextView) itemView.findViewById(R.id.size);
            }
        }
    }

    //获取缩略图
    public Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever media =new MediaMetadataRetriever();
        media.setDataSource(videoPath);
        Bitmap bitmap = media.getFrameAtTime();
        return bitmap;
    }
}
