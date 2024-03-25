package com.example.DogBreeds;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class postcomment extends Fragment {

    public RecyclerView commentiteam;
    FirebaseFirestore firebaseFirestore;
    List<comment> commentlist;
    postcommentadaptor postcommentadaptor;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_postcomment, container, false);

       commentlist =new ArrayList<>();
       Bundle bundle=new Bundle();
       bundle=getArguments();
       String blogpostid=getArguments().getString("postid");


        commentiteam=view.findViewById(R.id.commentiteam);

       postcommentadaptor=new postcommentadaptor(commentlist);
       commentiteam.setLayoutManager(new LinearLayoutManager(getActivity()));
       commentiteam.setAdapter(postcommentadaptor);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("post").document(blogpostid).collection("Comments").addSnapshotListener((documentSnapshots,e)-> {

            for (DocumentChange doc:documentSnapshots.getDocumentChanges()){
                if(doc.getType()==DocumentChange.Type.ADDED){
                    comment c=doc.getDocument().toObject(comment.class);
                    System.out.println("jjj"+c.com);
                    commentlist.add(c);
                    postcommentadaptor.notifyDataSetChanged();
                }
            }
        });

        return view;
    }
}