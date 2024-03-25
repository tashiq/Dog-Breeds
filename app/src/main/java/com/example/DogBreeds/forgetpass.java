package com.example.DogBreeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpass extends AppCompatActivity {
EditText pass;
Button submit;
ProgressBar progressBar;
FirebaseAuth myauth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpass);
        pass=(EditText) findViewById(R.id.pass);
        submit=(Button) findViewById(R.id.submit);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        myauth=FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reset();
            }
        });

    }

    private void reset() {
        String email=pass.getText().toString().trim();

        if(email.isEmpty()){
            pass.setError("please Enter Your email");
            pass.requestFocus();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            pass.setError("please provide correct email");
            pass.requestFocus();
        }
        progressBar.setVisibility(View.VISIBLE);
        myauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    pass.setText("");
                    Toast.makeText(getApplicationContext(),"Send email to your account",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Intent i=new Intent(getApplicationContext(),login.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Cant send email",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

        });

    }


}