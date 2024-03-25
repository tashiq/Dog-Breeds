package com.example.DogBreeds;

import androidx.annotation.NonNull;

public class blogpostid {
    public  String blogpostid;
    public  <T extends blogpostid> T withId(@NonNull final String id){
             this.blogpostid=id;
             return (T) this;
    }
}
