package com.example.DogBreeds;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class Predictlist extends AppCompatActivity {

    ArrayList<String> areaName=new ArrayList<>();
    HashSet<String> UserId=new HashSet<>();
    ArrayList <String>userid=new ArrayList<>();
    HashSet<String>numberpredictlist=new HashSet<>();
    ArrayList <String>numberpredict=new ArrayList<>();
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4;
    int f=1;
    ListView list,list1,list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        areaName.add("chittagong");
        areaName.add("chittagong");
        areaName.add("chittagong");
        setContentView(R.layout.activity_predictlist);
        databaseReference = FirebaseDatabase.getInstance().getReference("prediction");
        list = findViewById(R.id.listview);
        list1=findViewById(R.id.listview);
        list2=findViewById(R.id.listview);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                custombaseadapter custombaseadapter = new custombaseadapter(getApplicationContext(), areaName);
                list.setAdapter(custombaseadapter);

            }
        }, 4000);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    String f = s.getKey().toString();
                    areaName.add(f);
                    System.out.println(s.getKey());

                }
                System.out.println("tatapatha"+" "+areaName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




new Handler().postDelayed(new Runnable() {
   @Override
    public void run() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String f=areaName.get(position);
                System.out.println(f);
                databaseReference1=databaseReference.child(f);
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot d:snapshot.getChildren()){
                            UserId.add(d.getKey());
                        }
                        System.out.println("kk"+UserId);
                        userid=new ArrayList<>(UserId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList <String>userid=new ArrayList<>(UserId);
                        custombaseadapter custombaseadapter=new custombaseadapter(getApplicationContext(),userid);
                        list1.setAdapter(custombaseadapter);
                    }
                },3000);
            }


        });

    }
},1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        numberpredictlist.clear();
                        String s=userid.get(position);
                       databaseReference2=databaseReference1.child(s);
                       databaseReference2.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                               for(DataSnapshot t:snapshot.getChildren()){

                                   numberpredictlist.add(t.getKey());
                               }
                               System.out.println(numberpredictlist);
                               numberpredict=new ArrayList<>(numberpredictlist);
                           }


                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }
                       });

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                              ArrayList<String > numberpredict=new ArrayList<>(numberpredictlist);
                               System.out.println(numberpredict);
                               custombaseadapter custombaseadapter=new custombaseadapter(getApplicationContext(),numberpredict);
                               list2.setAdapter(custombaseadapter);
                           }
                       },1000);

                    }
                });

            }
        },9000);


new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println(numberpredict.get(position));
                 databaseReference3=databaseReference2.child(numberpredict.get(position));
                databaseReference3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String image = null,lat=null,lan=null;
                       for(DataSnapshot s:snapshot.getChildren()){
                           if(s.getKey().equals("image")){
                               image=s.getValue().toString();
                               System.out.println(image);
                           }
                           else if(s.getKey().equals("lat")){
                               lat=s.getValue().toString();
                           }
                           else{
                               lan=s.getValue().toString();
                           }
                       }
                        Intent intent=new Intent(getApplicationContext(),PredictionUser.class);
                      intent.putExtra("image",image);
                        intent.putExtra("lati",lat);
                        intent.putExtra("lang",lan);
                         startActivity(intent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
},12000);

    }

}