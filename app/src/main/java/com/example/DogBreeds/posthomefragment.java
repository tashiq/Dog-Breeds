package com.example.DogBreeds;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class posthomefragment extends Fragment {


 private RecyclerView blog_list_view;
 private List<blogpost>Blog_list;
 private List<blogpost>Blog_list2;
 private List<String>blogid;

 DatabaseReference databaseReference;
 private blogrecycleradapter blogrecycleradapter;
    FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot lastvisit;
 boolean firstpage=true;
 boolean isloading=false;
 String key=null;
 int totalcount=0;
int currentidcount=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View  view =inflater.inflate(R.layout.fragment_posthomefragment, container, false);
       Blog_list=new ArrayList<>();
       Blog_list2=new ArrayList<>();
       blogid=new ArrayList<>();
       blog_list_view=view.findViewById(R.id.blogiteam);
       //databaseReference= FirebaseDatabase.getInstance().getReference("Post");


        blogrecycleradapter=new blogrecycleradapter(Blog_list);
        blog_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        blog_list_view.setAdapter(blogrecycleradapter);

        firebaseFirestore=FirebaseFirestore.getInstance();



        blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                Boolean reachbottom=!recyclerView.canScrollVertically(1);

                if(reachbottom){

                    Toast.makeText(getActivity(),"Wait",Toast.LENGTH_SHORT).show();
                    loadmore();

                }
            }
        });




        Query firstquery=firebaseFirestore.collection("post").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);


        firstquery.addSnapshotListener(new EventListener<QuerySnapshot>() {

            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
             if(!value.isEmpty()){
                 if(firstpage) {

                     lastvisit = value.getDocuments().get(value.size() - 1);
                 }
                for(DocumentChange doc:value.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String blogpostid = doc.getDocument().getId();
                        blogpost blog = doc.getDocument().toObject(blogpost.class).withId(blogpostid);
                        System.out.println("time"+blog.getTimestamp());
                        if(firstpage) {
                            Blog_list.add(blog);
                        }
                        else{
                            Blog_list.add(0,blog);
                        }
                        blogrecycleradapter.notifyDataSetChanged();
                    }
                }
                firstpage=false;
             }
            }
        });



      return view;
    }



    public  void loadmore(){
        Query nextquery=firebaseFirestore.collection("post").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastvisit)
                .limit(3);


        nextquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(!value.isEmpty()) {

                   lastvisit = value.getDocuments().get(value.size() - 1);
                   for (DocumentChange doc : value.getDocumentChanges()) {
                       if (doc.getType() == DocumentChange.Type.ADDED) {
                           String blogpostid = doc.getDocument().getId();
                           blogpost blog = doc.getDocument().toObject(blogpost.class).withId(blogpostid);
                           Blog_list.add(blog);
                           blogrecycleradapter.notifyDataSetChanged();
                       }
                   }
               }            }
        });


    }

}