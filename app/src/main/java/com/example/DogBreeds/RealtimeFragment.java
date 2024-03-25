package com.example.DogBreeds;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RealtimeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // startActivity(new Intent(getActivity(),org.codeforiraq.machinelearning.MainActivity.class));
        return inflater.inflate(R.layout.fragment_realtime, container, false);
    }

//    public void onBackPressed(){
////        Fragment fragment = new DashboardFragment();
////        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////        fragmentTransaction.setReorderingAllowed(true);
////        fragmentTransaction.replace(R.id.fragment_container, fragment);
////        fragmentTransaction.commit();
//
//        AppCompatActivity activity=(AppCompatActivity) getActivity();
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).addToBackStack(null).commit();
//    }
}