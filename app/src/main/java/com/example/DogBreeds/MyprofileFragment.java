package com.example.DogBreeds;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class MyprofileFragment extends Fragment {

TextView name,email,phn,zilla,upozilla,union;
String na,em,ph,zi,up;
Button choose,upload;
ImageView image;
int picimagecode=1;
Uri imag;
int f=0;
StorageReference storageReference;
DatabaseReference databaseReference;
private StorageTask uploadTask;
    ProgressDialog dialog;

FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_myprofile, container, false);
        dialog=new ProgressDialog(getActivity());
        choose=view.findViewById(R.id.choose);
        upload=view.findViewById(R.id.upload);
        image=view.findViewById(R.id.imageview);
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        phn=view.findViewById(R.id.number);
        zilla=view.findViewById(R.id.zilla);
        upozilla=view.findViewById(R.id.upozilla);
        union=view.findViewById(R.id.union);

        storageReference= FirebaseStorage.getInstance().getReference("users");
        databaseReference=FirebaseDatabase.getInstance().getReference("users");



        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage();
            }
        });

      upload.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
             if(uploadTask!=null && uploadTask.isInProgress()){
               Toast.makeText(getActivity(),"Upload is in process",Toast.LENGTH_LONG).show();

             }else {
                 dialog.setMessage("Upload...");
                 dialog.show();
                 uploadImage();


             }
          }
      });

        dialog.dismiss();
        getImage();
        getalldata();

    return view;
    }

    private void getalldata() {
            DatabaseReference data=databaseReference.child(user.getUid());
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name.setText(snapshot.child("name").getValue().toString());
                    email.setText(snapshot.child("email").getValue().toString());
                    phn.setText(snapshot.child("phone").getValue().toString());
                    zilla.setText(snapshot.child("zilla").getValue().toString());
                    upozilla.setText(snapshot.child("upozilla").getValue().toString());
                    union.setText(snapshot.child("union").getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void getImage() {

        DatabaseReference getImage=databaseReference.child(user.getUid()).child("image");

        getImage.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot dataSnapshot)
                    {

                        String link = dataSnapshot.getValue(
                                String.class);


                        Picasso.get().load(link).into(image);

                    }

                    // this will called when any problem
                    // occurs in getting data
                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError databaseError)
                    {
                        // we are showing that error message in
                        // toast
                        dialog.dismiss();
                        Toast
                                .makeText(getActivity(),
                                        "Error Loading Image",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }




    private String getfileExtension(Uri uri){
        ContentResolver cr= getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {


       if(imag!=null){


       StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getfileExtension(imag));

           uploadTask=filereference.putFile(imag).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   if (taskSnapshot.getMetadata() != null) {
                       if (taskSnapshot.getMetadata().getReference() != null) {
                           Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                           result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {
                                   String imageUrl = uri.toString();
                                   FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                                   databaseReference.child(user.getUid()).child("image").setValue(imageUrl);
                                  dialog.dismiss();
                                   //createNewPost(imageUrl);
                                   f=1;
                               }

                           });

                       }
                   }

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(getActivity(),"No file",Toast.LENGTH_LONG).show();

               }
           });




       }



       else{
           Toast.makeText(getActivity(),"No file select",Toast.LENGTH_LONG).show();
       }

    }

    private void chooseimage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,picimagecode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==picimagecode && data!=null && data.getData()!=null){
             imag=data.getData();
            image.setImageURI(imag);
        }
    }



}