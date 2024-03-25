package com.example.DogBreeds;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout  drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView imageView;
    DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       drawerLayout=findViewById(R.id.drawerlayout);
       navigationView=findViewById(R.id.nav);
       toolbar=findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

       View headView=navigationView.getHeaderView(0);

        imageView=headView.findViewById(R.id.imageview);
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference getImage=databaseReference.child(user.getUid()).child("image");


        getImage.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot dataSnapshot)
                    {

                        String link = dataSnapshot.getValue(String.class);
                        Picasso.get().load(link).into(imageView);
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError databaseError)
                    {

                        Toast.makeText(getApplicationContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();
                    }
                });





        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer) ;
       drawerLayout.addDrawerListener(toggle);
       toggle.syncState();

       navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState!=null||savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.home){
            Toast.makeText(getApplicationContext(),"jkkhh",Toast.LENGTH_LONG).show();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).addToBackStack(null).commit();
            System.out.println("hhjhhhh");
        }
        else if(item.getItemId()==R.id.realtime){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RealtimeFragment()).addToBackStack(null).commit();
        }



      else if(item.getItemId()==R.id.soluton){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new solutionfragment()).addToBackStack(null).commit();
        }

       else if(item.getItemId()== R.id.messaging){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MessagingFragment()).addToBackStack(null).commit();
        }

        else if(item.getItemId()==R.id.tutorial){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new expandablelist()).addToBackStack(null).commit();
        }



      else if(item.getItemId()==R.id.extra){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExtraFragment()).addToBackStack(null).commit();
        }

        else if(item.getItemId()== R.id.about){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MyprofileFragment()).addToBackStack(null).commit();
        }

        else if(item.getItemId()== R.id.rate){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RatingFragment()).addToBackStack(null).commit();
        }


      else  if(item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),login.class));
            finish();
        }


         drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        else{
            super.onBackPressed();
        }

    }
}