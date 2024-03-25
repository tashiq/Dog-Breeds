package com.example.DogBreeds;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



//import com.example.DogBreeds.ml.RiceData;
import com.example.DogBreeds.ml.RiceData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;

import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class solutionfragment<locationResult> extends Fragment {

Button selectBtn,predictbtn,captureBtn,download;
TextView result;
Bitmap bitmap;
Uri uploadimage;
ImageView image;
String dis_Name,message,Imageurl,locality=null;
DatabaseReference reference,databaseReference;
StorageReference storageReference;
private StorageTask upload;
FusedLocationProviderClient mFusedLocationClient;
double latitudeTextView=0.0;
double longitTextView=0.0;
int PERMISSION_ID = 44;
List<Address> address;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_solutionfragment, container, false);
        String [] labels=new String[5];
        int cnt=0;
        try {
            AssetManager assetManager = getActivity().getResources().getAssets();
            InputStream inputStream = assetManager.open("labels.txt");
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line=bufferedReader.readLine();
            while(line!=null){
                labels[cnt]=line;
                cnt++;
                line=bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        getpermission();
        selectBtn=view.findViewById(R.id.selectBtn);
        predictbtn=view.findViewById(R.id.predictBtn);
        captureBtn=view.findViewById(R.id.captureBtn);
        result=view.findViewById(R.id.result);
        download=view.findViewById(R.id.download);
        image=view.findViewById(R.id.imageview);

        reference= FirebaseDatabase.getInstance().getReference("pdf");
        databaseReference=FirebaseDatabase.getInstance().getReference("prediction");
        storageReference= FirebaseStorage.getInstance().getReference("predictImage");
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,12);

            }
        });


        predictbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    RiceData model = RiceData.newInstance(getActivity());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.UINT8);
                    bitmap=Bitmap.createScaledBitmap(bitmap,224,224,true);
                    inputFeature0.loadBuffer(TensorImage.fromBitmap(bitmap).getBuffer());

                    // Runs model inference and gets result.
                    RiceData.Outputs outputs = model.process(inputFeature0);
                    System.out.println("DDD"+outputs);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    System.out.println("DDD"+outputFeature0);
                    dis_Name=labels[getMax(outputFeature0.getFloatArray())];
                    result.setText(labels[getMax(outputFeature0.getFloatArray())]+" ");

                    // Releases model resources if no longer used.
                    model.close();
                    downloadpdf();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

                // method to get the location

                        getLastLocation();
                        imageupload();

                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                              savedatabase();

                           }
                       },10000);

            }
        });

        return view;
    }

  synchronized  private void downloadpdf() {
        System.out.println(dis_Name);

        if(dis_Name!=null && !dis_Name.equals("Invalid")){
            download.setVisibility(View.VISIBLE);

            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    for(DataSnapshot di:snapshot.getChildren()){
                       String name=di.getKey().toString();
                       if(name.equals(dis_Name)){
                           message=di.getValue(String.class);
                           System.out.println(message);
                       }
                    }
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




//download pdf

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(message);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message));
                    startActivity(intent);
                }
            });
        }
    }



    int getMax(float[] arr){
        int max=0;
        for(int i=0;i<arr.length;i++)
            if(arr[i]>arr[max]) max=i;
        return max;
}




    void getpermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
               ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},11);
           }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==10){
            if(grantResults.length>0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getpermission();
                }
            }
        }

        if(requestCode==12){
            if(grantResults.length>0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.getpermission();
                }
            }
        }

       else if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(requestCode);
        if(requestCode==10){
            if(data!=null) {
                Uri uri = data.getData();
                try {
                    uploadimage=data.getData();
                    System.out.println("ttt"+uploadimage);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), uri);
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         if(requestCode==12){
             System.out.println("sksksks"+data);
             Uri uri=data.getData();
            uploadimage=data.getData();
            System.out.println("sksks"+uploadimage+" "+uri);
//            uploadimage= (Uri) data.getExtras().get("data");
            //System.out.println("sksks"+uploadimage+" "+uri);
            bitmap=(Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    //loc


    @SuppressLint("MissingPermission")
 synchronized    private void getLastLocation() {
        System.out.println("skssh");
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            System.out.println( latitudeTextView=location.getLatitude());
                            System.out.println(longitTextView=location.getLongitude());
                            Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                address=geocoder.getFromLocation(latitudeTextView,longitTextView,1);
                                System.out.println(address.get(0).getCountryName());
                                System.out.println(address.get(0).getCountryCode());
                                System.out.println(address.get(0).getAdminArea());
                                System.out.println(locality=address.get(0).getLocality());


                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });
            } else {
                Toast.makeText(getActivity(), "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            System.out.println(latitudeTextView= mLastLocation.getLatitude()) ;
            System.out.println(longitTextView= mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }
//    }



    public  void savedatabase(){

        System.out.println(Imageurl+latitudeTextView+ ""+longitTextView);
        if(Imageurl!=null && latitudeTextView!=0.0 && longitTextView!=0.0 && locality!=null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            predictiondetails predic = new predictiondetails(Imageurl, latitudeTextView, longitTextView);
            databaseReference.child(String.valueOf(locality)).child(user.getUid()).child(String.valueOf(System.currentTimeMillis())).setValue(predic);
            //System.out.println(databaseReference.child(String.valueOf(locality)).child(user.getUid()).child(String.valueOf(System.currentTimeMillis())).get());
            System.out.println("success");
        }
        else{
            System.out.println("djshs");
            Toast.makeText(getActivity(),"Data Not saved",Toast.LENGTH_LONG).show();
        }
    }

    private String getfileExtension(Uri uri){
        ContentResolver cr= getActivity().getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void imageupload(){
        if(uploadimage!=null){

            StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getfileExtension(uploadimage));

            upload=filereference.putFile(uploadimage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Imageurl = uri.toString();
                                    System.out.println(Imageurl);
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




}