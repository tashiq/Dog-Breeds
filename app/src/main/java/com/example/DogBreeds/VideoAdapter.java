package com.example.DogBreeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    private List<youTubeVideos> youtubeVideoList;
    VideoAdapter(List<youTubeVideos> youtubeVideoList) {

        this.youtubeVideoList = youtubeVideoList;

    }

    public void setItems(List<youTubeVideos> youtubeVideoList){
        this.youtubeVideoList = youtubeVideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.video_view, parent, false);
        return new VideoViewHolder(view);
    }




    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.videoWeb.loadData( youtubeVideoList.get(position).getVideoUrl(), "text/html" , "utf-8");
        String d=youtubeVideoList.get(position).getDescription();
        holder.vidiodescription.setText(d);

    }
    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        WebView videoWeb;
        TextView vidiodescription;
        VideoViewHolder(View itemView) {
            super(itemView);
            vidiodescription=itemView.findViewById(R.id.videpdescription);
            videoWeb = itemView.findViewById(R.id.webView);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {
            } );
        }
    }
}
