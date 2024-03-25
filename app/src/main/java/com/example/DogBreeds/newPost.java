package com.example.DogBreeds;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class newPost extends Fragment {

  private ImageView imageView;
  private EditText editText;
  private Button button;
    Uri uploadimage=null;
    Bitmap bitmap;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage;
    private StorageTask uploadTask;
    String post_id;
    private String  userid;
    private DatabaseReference databaseReference;
    FirebaseFirestore firebaseFirestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_new_post, container, false);

        storageReference=FirebaseStorage.getInstance().getReference("post_image");
        databaseReference=FirebaseDatabase.getInstance().getReference("Post");
        firebaseFirestore=FirebaseFirestore.getInstance();
        userid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        imageView=view.findViewById(R.id.newpostimage);
        editText=view.findViewById(R.id.description);
        button=view.findViewById(R.id.postbutton);
        progressBar=view.findViewById(R.id.newpostprogress);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,12);

            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description=editText.getText().toString();
                if(!description.isEmpty() && uploadimage!=null){
                        progressBar.setVisibility(View.VISIBLE);

                    StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getfileExtension(uploadimage));
                    if(uploadTask!=null && uploadTask.isInProgress()){
                        Toast.makeText(getActivity(),"Upload is in process",Toast.LENGTH_LONG).show();

                    }
                   else {
                        uploadTask = filereference.putFile(uploadimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                Map<String, Object> postmap = new HashMap<>();
                                                //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String currentDateTime =DateFormat.getDateTimeInstance().format(new Date());
                                                //= dateFormat.format(new Date());
                                                post_id = String.valueOf(System.currentTimeMillis());
                                                System.out.println(post_id);
                                                postmap.put("post_image", imageUrl);
                                                postmap.put("description", description);
                                                postmap.put("user_id", userid);
                                                postmap.put("timestamp", FieldValue.serverTimestamp());
                                               firebaseFirestore.collection("post").add(postmap);
                                               // databaseReference.child(post_id).setValue(postmap);
                                                progressBar.setVisibility(View.INVISIBLE);
                                                AppCompatActivity activity=(AppCompatActivity) getActivity();
                                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new newPost()).addToBackStack(null).commit();


                                            }

                                        });

                                    }
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });


                    }






                }
            }
        });

        return  view;
    }

    private String getfileExtension(Uri uri) {
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

        @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==12){
            if(data!=null) {
                Uri uri = data.getData();
                try {
                    uploadimage=data.getData();
                    System.out.println("ttt"+uploadimage);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                   e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);


    }
}