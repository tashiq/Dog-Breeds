package com.example.DogBreeds;







import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;


import org.codeforiraq.machinelearning.DemoActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;




public class DashboardFragment extends Fragment implements View.OnClickListener {
    CardView realtime, solution, messaging, extra;



    double latitudeTextView = 0.0;
    double longitTextView=0.0;
    private RequestQueue mQueue;
    String locationprovider = LocationManager.GPS_PROVIDER;
    LocationManager mlocationManager;
    LocationListener mlocationListener;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    String city="";
    TextView temperature,condition,sohor;
    ImageView weathericon;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        realtime = view.findViewById(R.id.realtime);
        solution = view.findViewById(R.id.soluton);
        messaging = view.findViewById(R.id.messaging);
        extra = view.findViewById(R.id.extra);
        temperature = view.findViewById(R.id.temperature);
        condition=view.findViewById(R.id.condition);
        sohor=view.findViewById(R.id.city);
        weathericon=view.findViewById(R.id.weatherimage);

        realtime.setOnClickListener(this);
        solution.setOnClickListener(this);
        messaging.setOnClickListener(this);
        extra.setOnClickListener(this);

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getLastLocation();
    }



    synchronized private void getLastLocation() {
        System.out.println("skssh");
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {

                            String lat = String.valueOf(location.getLatitude());
                            String lan = String.valueOf(location.getLongitude());


//                gotoaccessdata(params);

                        double latitudeTextView = location.getLatitude();
                        double longitTextView = location.getLongitude();
                            System.out.println(lan+lat);
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                List<Address> address = geocoder.getFromLocation(latitudeTextView,longitTextView, 1);
                               /// String country=address.get(0).getCountryName();
//                                System.out.println(address.get(0).getCountryCode());
//                                System.out.println(address.get(0).getAdminArea());
                                System.out.println(address);
                                if(!address.isEmpty()) {
                                    String city = address.get(0).getLocality();

                                    if(city==null){
                                        gotoaccessdata(address.get(0).getAdminArea());

                                    }
                                    else {
                                        System.out.println("dddd" + city);
                                        gotoaccessdata(city);

                                    }

                                }
                                else{
                                    System.out.println("empty");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //if(!city.isEmpty())
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    gotoaccessdata(city);
//                                }
//                            },2000);



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
            String lat = String.valueOf(mLastLocation.getLatitude());
            String lan = String.valueOf(mLastLocation.getLongitude());


            //gotoaccessdata(params);

            System.out.println(latitudeTextView= mLastLocation.getLatitude()) ;
            System.out.println(longitTextView= mLastLocation.getLongitude());
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }





    private void gotoaccessdata(String city) {

        System.out.println(city);
        String url="http://api.weatherapi.com/v1/forecast.json?key=8a2d7c4826ec49beab793752232102&q="+ city +"&days=1&aqi=yes&alerts=yes";
        sohor.setText(city);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String tem=response.getJSONObject("current").getString("temp_c");
                    temperature.setText(tem+" °C");

                    String cond=response.getJSONObject("current").getJSONObject("condition").getString("text");
                    condition.setText(cond);
                    String icon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(icon)).into(weathericon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            // this is the error listener method which
            // we will call if we get any error from API.
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
                Toast.makeText(getContext(), "Fail to get data..", Toast.LENGTH_SHORT).show();
            }

        });

        mQueue.add(jsonObjectRequest);
    }





//    public void update(weatherdata weatherdata){
//        temperature.setText(weatherdata.getMtem());
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSION_ID){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
               Toast.makeText(getActivity(),"Location granted",Toast.LENGTH_LONG).show();
                getLastLocation();
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == realtime.getId()) {
            System.out.println(realtime.getId()+" "+view.getId());
            Intent i=new Intent();
            i.setClass(getActivity().getApplicationContext(), DemoActivity.class);
            startActivity(i);
           // AppCompatActivity activity=(AppCompatActivity) getActivity();
            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RealtimeFragment()).addToBackStack(null).commit();

        }
        if (view.getId() == solution.getId()) {
            System.out.println(solution.getId()+" "+view.getId());
            AppCompatActivity activity=(AppCompatActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new solutionfragment()).addToBackStack(null).commit();

        }

        if (view.getId() == messaging.getId()) {
            System.out.println(messaging.getId()+" "+view.getId());
            AppCompatActivity activity=(AppCompatActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MessagingFragment()).addToBackStack(null).commit();

        }


        if (view.getId() == extra.getId()) {
            System.out.println(extra.getId()+" "+view.getId());
            AppCompatActivity activity=(AppCompatActivity) getActivity();
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ExtraFragment()).addToBackStack(null).commit();

        }


    }

}
