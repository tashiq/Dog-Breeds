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
import java.util.HashMap;

public class DataArray extends AppCompatActivity {
    ArrayList<String> areaName=new ArrayList<>();
    ArrayList<String> userid=new ArrayList<>();
    ArrayList<String> userid1=new ArrayList<>();
    HashMap<String, ArrayList<String> >useridH
            = new HashMap<String, ArrayList<String>>();
    ArrayList <String>postNumber=new ArrayList<>();
    HashMap<String, ArrayList<String> >postNumberH
            = new HashMap<String, ArrayList<String>>();
    ArrayList <String>s=new ArrayList<>() ;
    ArrayList <String>t=new ArrayList<>() ;
    ListView list,list1,list2;
    String area,id;
   int i;
    int j=0;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,databaseReference3,databaseReference4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_array);
        databaseReference = FirebaseDatabase.getInstance().getReference("prediction");

         list=findViewById(R.id.listview);
        list1=findViewById(R.id.listview);
        list2=findViewById(R.id.listview);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot a:snapshot.getChildren()){
                        area = a.getKey();
                        areaName.add(area);
                        System.out.println(area);
                }
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       setareaAdaptor();
                   }
               },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setidadaptor();
                    }
                },1300);
                //setidadaptor();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

  synchronized   private void setidadaptor() {

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(areaName.get(position));
                s.clear();
                s=getiddata(areaName.get(position));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       finalsetidadapter();
                    }
                },1000);
            }
        });


    }

  synchronized   private void finalsetidadapter() {
        custombaseadapter custombaseadapter = new custombaseadapter(getApplicationContext(), userid);
        list1.setAdapter(custombaseadapter);
        setnumberofpost();
    }

  synchronized   private void setnumberofpost() {
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      t.clear();
                     t =getnumberofpost(userid.get(position));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finalsetnumberofpost();
                    }
                },1000);

            }
        });
    }

   synchronized private void finalsetnumberofpost() {
       custombaseadapter custombaseadapter = new custombaseadapter(getApplicationContext(), t);
       list2.setAdapter(custombaseadapter);
       getlocation();
    }

    private void getlocation() {
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                databaseReference3=databaseReference2.child(t.get(position));
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

    synchronized private ArrayList<String> getnumberofpost(String s) {
         databaseReference2=databaseReference1.child(s);
         databaseReference2.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for(DataSnapshot i:snapshot.getChildren()){
                      postNumber.add(i.getKey());
                  }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });
  return (ArrayList<String>) postNumber;
    }

    synchronized   private ArrayList<String> getiddata(String s) {
        databaseReference1=databaseReference.child(s);
      databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userid.clear();
                for (DataSnapshot i : snapshot.getChildren()) {
                    //System.out.println(i.getKey());
                    userid.add(i.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
      return (ArrayList<String>) userid;

    }

    synchronized private void setareaAdaptor() {
       System.out.println(areaName);
        custombaseadapter custombaseadapter = new custombaseadapter(getApplicationContext(), areaName);
        list.setAdapter(custombaseadapter);
    }














//    synchronized  private void ttt(String area) {
//        databaseReference1 = databaseReference.child(area);
//
//    }
}