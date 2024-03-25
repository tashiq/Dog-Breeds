package com.example.DogBreeds;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Pdfuploader extends AppCompatActivity {
    StorageReference storage;
   DatabaseReference reference;
   String diseasename="";
   Button pdf;
    EditText text;
    Uri imageuri;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfuploader);

        pdf=findViewById(R.id.pdf);
        text=findViewById(R.id.text);

        System.out.println(diseasename);
        storage=FirebaseStorage.getInstance().getReference("pdf");
        reference= FirebaseDatabase.getInstance().getReference("pdf");

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                 intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
               startActivityForResult(intent,101);
            }
        });
    }

    ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            diseasename=text.getText().toString();
            dialog=new ProgressDialog(this);
            dialog.setMessage("uploading");
            dialog.show();

            imageuri=data.getData();
            System.out.println(imageuri);
            System.out.println(diseasename);
            StorageReference storageReference=storage.child(System.currentTimeMillis()+"."+"pdf");
          storageReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  if (taskSnapshot.getMetadata() != null) {
                      if (taskSnapshot.getMetadata().getReference() != null) {
                          Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                          result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  String pdfUrl = uri.toString();
                                  FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                  System.out.println(diseasename);
                                  reference.child("Disease").child(diseasename).setValue(pdfUrl);
                                  //createNewPost(imageUrl);
                                  dialog.dismiss();
                              }

                          });

                      }
                  }

              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(),"Fail to upload",Toast.LENGTH_LONG).show();
              }
          });


        }

        else{
            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
        }



    }
}