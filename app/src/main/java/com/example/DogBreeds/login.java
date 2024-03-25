package com.example.DogBreeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class login extends AppCompatActivity implements  View.OnClickListener {

TextView reg,forget,pass;
AutoCompleteTextView email;
Button button;
ProgressDialog progressDialog;
FirebaseAuth myauth;
DatabaseReference refemail;
ArrayList<String> emaill;
ArrayAdapter<String> arrayAdapteremail;
//ArrayList<String> emaill=new ArrayList<>();
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myauth=FirebaseAuth.getInstance();
        reg=findViewById(R.id.reg);
        forget=findViewById(R.id.forget);
        button=findViewById(R.id.button);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);



        refemail = FirebaseDatabase.getInstance().getReference("users");
        refemail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emaill = new ArrayList<>();
                for(DataSnapshot s:snapshot.getChildren()){
                    String e=s.child("email").getValue().toString();
                    emaill.add(e);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapteremail = new ArrayAdapter<>(getApplicationContext(),R.layout.text_view, emaill);
                        email.setAdapter(arrayAdapteremail);
                    }
                },1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reg.setOnClickListener(this);
        forget.setOnClickListener(this);
        button.setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.forget){
            Intent i=new Intent(getApplicationContext(),forgetpass.class);
            startActivity(i);
        }

        else if(view.getId()==R.id.reg){
            Intent i=new Intent(getApplicationContext(),Register_activity.class);
            startActivity(i);
        }
        else if(view.getId()==R.id.button){
            String em=email.getText().toString().trim();
            String password=pass.getText().toString().trim();
            if(em.isEmpty()){

                email.setError("Please Enter Your email");
                email.requestFocus();
                return ;
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(em).matches()){
                email.setError("Please Enter correct email");
                email.requestFocus();
                return ;
            }

            if(password.isEmpty()||password.length()<8){
                pass.setError("Please enter atleast 8 length password");
                pass.requestFocus();
                return ;
            }
            progressDialog=new ProgressDialog(login.this);

            progressDialog.show();

            progressDialog.setContentView(R.layout.progress_bar);
            progressDialog.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );
            myauth.signInWithEmailAndPassword(em,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                               if(task.isSuccessful()){
                                   FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                   System.out.println(user.getUid());

                                   if(user.isEmailVerified()){
                                       Toast.makeText(getApplicationContext(),"Successfully login",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                       //progressBar.setVisibility(view.GONE);
                                       if(user.getUid().equals("YKAGdHXEyjQfZP0GZvV4NPfNysi2")){
                                           startActivity(new Intent(getApplicationContext(),DataArray.class));
                                       }
                                       else {
                                           startActivity(new Intent(getApplicationContext(), home.class));
                                           //startActivity(new Intent(getApplicationContext(),Pdfuploader.class));
                                           finish();
                                       }
                                   }
                                   else{
                                       progressDialog.dismiss();
                                       //progressBar.setVisibility(view.GONE);
                                       user.sendEmailVerification();
                                       myauth.signOut();
                                       showverifyAlertDialog();
                                       //Toast.makeText(getApplicationContext(),"check your mail to verify",Toast.LENGTH_LONG).show();

                                   }
                               }
                               else{
                                   String errormessage=task.getException().getMessage();
                                   progressDialog.dismiss();
                                   //progressBar.setVisibility(view.GONE);
                                   Toast.makeText(getApplicationContext(),"Failed to log in because of "+errormessage,Toast.LENGTH_LONG).show();
                               }
                        }
                    });

        }
    }

    private void showverifyAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(login.this);

        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify yoyr email now.You cant log in without email verification");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
           AlertDialog alertDialog= builder.create();
           alertDialog.show();
    }
}