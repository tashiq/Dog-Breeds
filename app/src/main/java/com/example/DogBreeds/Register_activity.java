package com.example.DogBreeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_activity extends AppCompatActivity implements  View.OnClickListener {
    String tha,up;
    DatabaseReference refdis,refupo,refuni,refemail,a;
    String district,thana,union,image;
    TextView textView1,textView2;
    Spinner districtspinner,thanaspinner,unionspinner;
    DatePicker datepicker;
    EditText nm,phn,pass,repass,date;
    AutoCompleteTextView em;
    Button button;
    //ProgressBar progressBar;
    ArrayList<String> dis;
    ArrayList<String>upo;
    ArrayList<String> uni;
    ArrayList<String> email;

    ProgressDialog progressDialog;

    ArrayAdapter<String> arrayAdapterx;
    ArrayAdapter<String>arrayAdaptery;
    ArrayAdapter<String> arrayAdapterz;
    ArrayAdapter<String> arrayAdapteremail;

  private FirebaseAuth myauth;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        em = (AutoCompleteTextView) findViewById(R.id.email);
        districtspinner = (Spinner) findViewById(R.id.districtspinner);
        thanaspinner = (Spinner) findViewById(R.id.thanaspinner);
        unionspinner = findViewById(R.id.unionspinner);

        datepicker=findViewById(R.id.datepicker);
        date=findViewById(R.id.date);

        dis = new ArrayList<>();
        upo = new ArrayList<>();
        uni = new ArrayList<>();
        arrayAdapterx = new ArrayAdapter<>(getApplicationContext(), R.layout.text_view, dis);
        arrayAdaptery = new ArrayAdapter<>(getApplicationContext(), R.layout.text_view, upo);
        arrayAdapterz = new ArrayAdapter<>(getApplicationContext(), R.layout.text_view, uni);
        districtspinner.setAdapter(arrayAdapterx);
        thanaspinner.setAdapter(arrayAdaptery);
        unionspinner.setAdapter(arrayAdapterz);





        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepicker.setVisibility(View.VISIBLE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datepicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String val=view.getDayOfMonth()+"/"+view.getMonth()+"/"+view.getYear();
                    date.setText(val);
                    datepicker.setVisibility(View.GONE);
                }
            });
        }





        //district in adaptor
        refdis = FirebaseDatabase.getInstance().getReference("District");



        refdis.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dis.clear();

                for (DataSnapshot dist : snapshot.getChildren()) {
                    dis.add(dist.getValue().toString());
                    System.out.println(dist.getValue().toString());
                }
                arrayAdapterx.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // add email in arraylist

        refemail = FirebaseDatabase.getInstance().getReference("users");
        refemail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = new ArrayList<>();
                for(DataSnapshot s:snapshot.getChildren()){
                    String e=s.child("email").getValue().toString();
                    email.add(e);
                }
               new Handler().postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       arrayAdapteremail = new ArrayAdapter<>(getApplicationContext(),R.layout.text_view, email);
                       em.setAdapter(arrayAdapteremail);
                   }
               },1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    //selected distric

        districtspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                district = districtspinner.getSelectedItem().toString();
                // System.out.println(district);
                selectUpo(district);
                arrayAdaptery.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //authentication
        myauth = FirebaseAuth.getInstance();
        nm = (EditText) findViewById(R.id.name);
        phn = (EditText) findViewById(R.id.phone);
        pass = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.repeatpassword);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);


        //progressBar=(ProgressBar)findViewById(R.id.pros) ;



        thanaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                thana = thanaspinner.getSelectedItem().toString();
                selectuni(thana);
                arrayAdapterz.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        unionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                union = unionspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    private void selectuni(String thana) {
        uni.clear();
        refuni=FirebaseDatabase.getInstance().getReference("Union");
        refuni.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot unio:snapshot.getChildren()){
                    String d=snapshot.getKey().toString();
                    if(d.equals(thana)){
                        uni.add(unio.getValue().toString());
                    }
                }
                arrayAdapterz.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });










   }

    private void selectUpo(String district) {
        upo.clear();
        refupo=FirebaseDatabase.getInstance().getReference("Upozilla");
        refupo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                   for(DataSnapshot upozi:snapshot.getChildren()){
                       String d=snapshot.getKey().toString();
                      // System.out.println(d+" "+district);
                       if(d.equals(district)){
                           //System.out.println(upoz.getValue());
                              upo.add(upozi.getValue().toString());
                       }
                   }
                arrayAdaptery.notifyDataSetChanged();
                //System.out.println(upo.toArray().length);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }




    @Override
    public void onClick(View view) {
          if(view.getId()==R.id.button){
              submit();
          }
    }


    public void submit() {


        ///System.out.println(district+" "+thana);


        String name=nm.getText().toString().trim();
        String phone=phn.getText().toString().trim();
        String email=em.getText().toString().trim();
        String password=pass.getText().toString().trim();
        String repassword=repass.getText().toString().trim();
        String mobile="[0][1][0-9]{9}";
        Matcher mobilematch;
        Pattern mobilePat=Pattern.compile(mobile);
        mobilematch=mobilePat.matcher(phone);
        String dat=date.getText().toString();

        //String tana=thanaspinner.getSelectedItem().toString().trim();
       // System.out.println(district+thana);
        System.out.println(password+repassword);

        if(name.isEmpty()){
            nm.setError("Please Enter YOur Name");
            nm.requestFocus();
              return ;
        }
        if(phone.isEmpty()||phone.length()<11){
            phn.setError("Please Enter Your phone Number");
            phn.requestFocus();
            return ;
        }

        if(!mobilematch.find()){
            phn.setError("Please Enter correct phone Number");
            phn.requestFocus();
            return ;
        }

        if(email.isEmpty()){

            em.setError("Please Enter YOur email");
            em.requestFocus();
            return ;
         }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            em.setError("Please Enter correct email");
            em.requestFocus();
            return ;
        }

       if(district!=null) {
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if (district.equals("Select")) {

                       TextView errorText = (TextView) districtspinner.getSelectedView();
                       errorText.setError("");
                       districtspinner.requestFocus();

                   }
                   else{

                       System.out.println(tha);
                   }
               }
           },1000);

       }


       if(thana.equals("Select")){
           TextView errorText = (TextView) thanaspinner.getSelectedView();
           errorText.setError("");
           thanaspinner.requestFocus();
           System.out.println("kkkkkk");
           return;
       }


       if(dat.isEmpty()){
           date.setError("please enter your bithday");
           date.requestFocus();
           return ;
       }


//       if(union.equals("Select")||union.equals("")){
//           TextView errorText = (TextView)unionspinner.getSelectedView();
//           errorText.setError("");
//           unionspinner.requestFocus();
//           return ;
//       }


                if(password.isEmpty()||password.length()<8){
                    pass.setError("Please enter atleast 8 length password");
                    pass.requestFocus();
                    return ;
                }
                if(!password.equals(repassword)){
                    repass.setError("Please Enter correct pass");
                    repass.requestFocus();
                    return ;
                }






        progressDialog=new ProgressDialog(Register_activity.this);

                progressDialog.show();

        progressDialog.setContentView(R.layout.progress_bar);
      progressDialog.getWindow().setBackgroundDrawableResource(
              android.R.color.transparent
      );

      myauth.createUserWithEmailAndPassword(email,password)
              .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          User user_detail = new User(name,phone,email,district,thana,union,image,dat);

                          FirebaseDatabase database=FirebaseDatabase.getInstance();

                          DatabaseReference dat,data;
                          dat =database.getReference("users");
                          data=dat.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                          System.out.println(data);
                          data.setValue(user_detail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if(task.isSuccessful()){
                                              Toast.makeText(getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
                                             // progressBar.setVisibility(View.GONE);
                                             progressDialog.dismiss();
                                             startActivity(new Intent(getApplicationContext(),login.class));
                                          }
                                          else{
                                              System.out.println("kkk");
                                              Toast.makeText(getApplicationContext(),"Registration Not complete",Toast.LENGTH_SHORT).show();
                                              //progressBar.setVisibility(View.GONE);
                                              progressDialog.dismiss();
                                              System.out.println("eroor"+task.getException().getMessage());
                                          }
                                      }
                                  });
                      }
                      else{

                          Toast.makeText(getApplicationContext(),"Registration Not complete",Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                          // progressBar.setVisibility(View.GONE);
                      }
                  }
              });



    }




}