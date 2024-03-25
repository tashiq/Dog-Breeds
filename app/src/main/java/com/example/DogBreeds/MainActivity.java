 package com.example.DogBreeds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

 public class MainActivity extends AppCompatActivity {

    TextView textView;
    String s="কৃষক ও কৃষি";
            ;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Handler handler=new Handler();
             handler.postDelayed(new Runnable() {
         @Override
         public void run() {
             Intent i=new Intent(MainActivity.this,login.class);
             startActivity(i);
             finish();
         }
     },3000);

     textView=(TextView) findViewById(R.id.text);

        init(s);

    }

     private void init(String s) {
        if(i<s.length()){
            String d=textView.getText().toString();
            char f= s.charAt(i);
            d+=f;

            textView.setText(d);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    i++;
                    init(s);

                }
            },250);

        }
     }

 }