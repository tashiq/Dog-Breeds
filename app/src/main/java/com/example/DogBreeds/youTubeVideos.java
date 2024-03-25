package com.example.DogBreeds;

public class youTubeVideos {

    String videoUrl,description;
    public youTubeVideos() {
    }
    public youTubeVideos(String videoUrl,String description) {
        this.videoUrl = videoUrl;
        this.description=description;
    }



    public String getVideoUrl() {
        return videoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
