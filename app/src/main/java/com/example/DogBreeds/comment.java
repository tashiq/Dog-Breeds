package com.example.DogBreeds;

public class comment {
public String uid,com;


public comment(){}
    public comment(String uid, String com) {
        this.uid = uid;
        this.com= com;
    }

    public String getUid() {
        return uid;
    }

    public String getComment() {
        return com;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setComment(String comment) {
        this.com = comment;
    }
}
