package com.example.DogBreeds;

import java.util.Date;

public class blogpost extends blogpostid {
    public String user_id,post_image,description;
    public Date timestamp;
    public blogpost(){}

    public blogpost(String user_id, String post_image, String description, Date timestamp) {
        this.user_id = user_id;
        this.post_image = post_image;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getPost_image() {
        return post_image;
    }

    public String getDescription() {
        return description;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
