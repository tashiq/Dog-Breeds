package com.example.DogBreeds;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MessagingFragment extends Fragment {
private FloatingActionButton floatingActionButton;
private BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_messaging, container, false);

        bottomNavigationView=view.findViewById(R.id.bottomnavigation);
        floatingActionButton=view.findViewById((R.id.addpostbutton));

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe, new posthomefragment()).addToBackStack(null).commit();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity=(AppCompatActivity) getActivity();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe,new newPost()).addToBackStack(null).commit();
            }
        });

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if (R.id.home == item.getItemId()) {
                   AppCompatActivity activity = (AppCompatActivity) getActivity();
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe, new posthomefragment()).addToBackStack(null).commit();
                   return true;
               }
               else if (R.id.notification == item.getItemId()){
                   AppCompatActivity activity = (AppCompatActivity) getActivity();
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe, new notificationfragment()).addToBackStack(null).commit();
                   return true;
               }



               else if( R.id.account==item.getItemId()) {
                   AppCompatActivity activity = (AppCompatActivity) getActivity();
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe, new MyprofileFragment()).addToBackStack(null).commit();
                   return true;
               }
               return false;
           }

       });


     return view;
    }



}