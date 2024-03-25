package com.example.DogBreeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RatingFragment extends Fragment {



RatingBar ratingbar;
TextView textView;
String  rate;
String uid;
DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 View view =inflater.inflate(R.layout.fragment_rating, container, false);

        ratingbar=view.findViewById(R.id.ratingBar);
        textView=view.findViewById(R.id.ratenumber);
       uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("Rating");

        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                  String d=snapshot.child("rate").getValue().toString();
                    textView.setText(d);
                    float k=Float.valueOf(d);
                    ratingbar.setRating(k);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textView.setText("Rate "+(int)rating);
                rate=String.valueOf(rating);


                Map<String,String> ratingdata=new HashMap<>();
                ratingdata.put("rate",rate);


                databaseReference.child(uid).setValue(ratingdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(getContext(),"Rating Change to "+rate,Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });





        return view;
    }
}