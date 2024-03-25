package com.example.DogBreeds;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class web extends AppCompatActivity {
 WebView web;
    String value;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        web = findViewById(R.id.webview);
        WebSettings websettings=web.getSettings();
        websettings.setBuiltInZoomControls(true);
        websettings.setJavaScriptEnabled(true);

        Intent intent = getIntent();

            value = intent.getStringExtra("key");
            // Do something with the value
        System.out.println(value);

        if(value.equals("Tomato")) {
            web.loadUrl("file:///android_asset/index.html");
        }
    }
}