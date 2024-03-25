package com.example.DogBreeds;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PredictionUser extends AppCompatActivity {

    String image, lat, lan, query;
    Double ii,jj;
    ImageView imageView;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_user);
        imageView = findViewById(R.id.im);
        button = findViewById(R.id.loc);
        Intent intent = this.getIntent();
        if (intent != null) {
            image = intent.getStringExtra("image");
            lat = intent.getStringExtra("lati");
            ii=  Double.parseDouble(lat);
            lan = intent.getStringExtra("lang");
            jj= Double.parseDouble(lan);
            System.out.println(image);
        } else {
            System.out.println("djdhdgdjhsd");
        }

        Picasso.get().load(image).into(imageView);
        query = "https://www.google.com/maps/place/" + ii + "," + jj;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchword(query);
            }
        });


    }


    private void searchword(String word) {
        try {
            System.out.println(ii+" "+jj);
            System.out.println(word);
            Intent i = new Intent(Intent.ACTION_WEB_SEARCH);
            i.putExtra(SearchManager.QUERY, word);
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            searchNetCompat(word);
        }
    }

    private void searchNetCompat(String word) {
        try {
            Uri uri = Uri.parse("https://www.google.com/maps/place/"+ii +","+jj);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "EROOR", Toast.LENGTH_LONG).show();
        }
    }
}