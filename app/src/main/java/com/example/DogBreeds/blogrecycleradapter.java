package com.example.DogBreeds;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class blogrecycleradapter extends RecyclerView.Adapter<blogrecycleradapter.ViewHolder> {

    public List<blogpost>Blog_list;
    public Context context;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    long k;
    public  blogrecycleradapter(List<blogpost>Blog_list){
        this.Blog_list=Blog_list;
        //System.out.println(Blog_list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bloglistitem,parent,false);
        context=parent.getContext();
        firebaseAuth=FirebaseAuth.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        String blogpostid=Blog_list.get(position).blogpostid;
        String uid=firebaseAuth.getCurrentUser().getUid();

         String descdata=Blog_list.get(position).getDescription();
         holder.setdesctext(descdata);
         String image=Blog_list.get(position).getPost_image();
         holder.settimage(image);
         String userid=Blog_list.get(position).getUser_id().toString();
         String [] d={null};
         String [] s={null};
        databaseReference2=FirebaseDatabase.getInstance().getReference("Likes");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    d[0] =  snapshot.child("name").getValue().toString();
                    s[0]=snapshot.child("image").getValue().toString();
                  //System.out.println(d[0]);

                }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       Handler handler=new Handler();
       handler.postDelayed(new Runnable() {
           @Override
           public void run() {
               System.out.println(d[0]);
               holder.settname(d[0]);
               holder.setprofileimage(s[0]);
           }
       },100);


//      Date tim=Blog_list.get(position).getTimestamp();
//        System.out.println(tim);
//         long  time=tim.getTime();
//        System.out.println(time);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//         String dd= formatter.format(calendar.getTime()).toString();
//         holder.settime(dd);
//        }


        //thumbs down with fill not not

        db.collection("post").document(blogpostid).collection("Dislike").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    holder.dislikebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_down_24));
                }
                else{
                    holder.dislikebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_down_off_alt_24));

                }
            }
        });


        //dislike count

        db.collection("post").document(blogpostid).collection("Dislike").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    int cnt=value.size();
                    holder.setDislikecount(cnt);
                }
                else{
                    holder.setDislikecount(0);
                }

            }
        });




       //dislike
        holder.dislikebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("post").document(blogpostid).collection("Dislike").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(!task.getResult().exists()){

                            db.collection("post").document(blogpostid).collection("Likes").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                 if(task.getResult().exists()){
                                     db.collection("post").document(blogpostid).collection("Likes").document(uid).delete();
                                 }

                                }
                            });



                            Map<String,Object> Dislike=new HashMap<>();
                            Date instant=new Date();
                            Dislike.put("timestamp",instant);
                            db.collection("post").document(blogpostid).collection("Dislike").document(uid).set(Dislike);

//                           databaseReference2.child(blogpostid).child(uid).child("timestamp").setValue(instant);
                            holder.dislikebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_down_24));
                        }
                        else{
                            System.out.println("delete");
                            db.collection("post").document(blogpostid).collection("Dislike").document(uid).delete();
                            //databaseReference2.child(blogpostid).child(uid).removeValue();
                            holder.dislikebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_down_off_alt_24));
                        }
                    }
                });

            }
        });








       //likecount
        db.collection("post").document(blogpostid).collection("Likes").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
             if(!value.isEmpty()){
                 int cnt=value.size();
                 holder.setlikecount(cnt);
             }
           else{
                 holder.setlikecount(0);
             }

            }
        });




       //get thums up empty or not
        db.collection("post").document(blogpostid).collection("Likes").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                   if(value.exists()){
                       holder.likebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_up_24));

                   }
                   else{
                       holder.likebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_up_off_alt_24));

                   }
            }
        });


       //like
       holder.likebutton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               System.out.println(uid+" "+blogpostid);

               db.collection("post").document(blogpostid).collection("Likes").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(!task.getResult().exists()){

                           db.collection("post").document(blogpostid).collection("Dislike").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                   if(task.getResult().exists()){
                                       db.collection("post").document(blogpostid).collection("Dislike").document(uid).delete();
                                   }

                               }
                           });

                           Map<String,Object> like=new HashMap<>();
                           Date instant=new Date();
                           like.put("timestamp",instant);
                           db.collection("post").document(blogpostid).collection("Likes").document(uid).set(like);
//                           databaseReference2.child(blogpostid).child(uid).child("timestamp").setValue(instant);
                           holder.likebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_up_24));
                       }
                       else{
                           System.out.println("delete");
                           db.collection("post").document(blogpostid).collection("Likes").document(uid).delete();
                           //databaseReference2.child(blogpostid).child(uid).removeValue();
                           holder.likebutton.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_thumb_up_off_alt_24));
                       }
                   }
               });

           }
       });


//comment

        holder.commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("kire");
                holder.writecomment.setVisibility(View.VISIBLE);
                holder.clickcomment.setVisibility(View.VISIBLE);



            }
        });


         holder.clickcomment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String s=holder.writecomment.getText().toString();
                                if(!s.isEmpty()){
                                    holder.writecomment.setText(null);
                                    Map<String,Object> comment=new HashMap<>();
                                    comment.put("com",s);
                                    comment.put("uid",uid);
                                    Date d=new Date();
                                    String l=String.valueOf(d);
                                    db.collection("post").document(blogpostid).collection("Comments").document(l).set(comment);
                                }
                            }
                        });



//comment count

        db.collection("post").document(blogpostid).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(!value.isEmpty()){
                    System.out.println("dsd"+value);
                    int cnt=value.size();
                    holder.setcommentcount(cnt);
                }
                else{
                    holder.setcommentcount(0);
                }

            }
        });

//comments show

        holder.commentcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
               // activity.getSupportFragmentManager().beginTransaction().replace(R.id.bottomframe, new postcomment()).addToBackStack(null).commit();

                Fragment fragment=new postcomment();
                Bundle bundle=new Bundle();
                FragmentTransaction fragmentTransaction=activity.getSupportFragmentManager().beginTransaction();
                bundle.putString("postid",blogpostid);
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.bottomframe, fragment);
                fragmentTransaction.commit();

            }
        });





    }

    @Override
    public int getItemCount() {
        return Blog_list.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        private  View mview;
        EditText writecommment;
        private TextView descview,nameview,timeview,likecount,dislikecount,commentcount,writecomment;
        private ImageView imageView,pfimage,likebutton,dislikebutton,commentbutton,clickcomment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           mview=itemView;
           likebutton=mview.findViewById(R.id.likeimage);
            likecount=mview.findViewById(R.id.likecount);
            dislikebutton=mview.findViewById(R.id.dislikeimage);
            dislikecount=mview.findViewById(R.id.dislikecount);
            commentbutton=mview.findViewById(R.id.commentbutton);
            commentcount=mview.findViewById(R.id.commentcount);
            writecomment=mview.findViewById(R.id.writecomment);
            clickcomment=mview.findViewById(R.id.clickcomment);
        }
        public void setdesctext(String s){
            descview =mview.findViewById(R.id.postdescription);
            descview.setText(s);
        }

        public void settimage(String ima){
            imageView=mview.findViewById(R.id.postimage);
            Picasso.get().load(ima).into(imageView);
        }
        public void settname(String nam){
            nameview=mview.findViewById(R.id.username);
            nameview.setText(nam);
        }

        public void settime(String ti){
            nameview=mview.findViewById(R.id.timestamp);
            nameview.setText(ti);
        }
        public  void setprofileimage(String pfimage){
            imageView=mview.findViewById(R.id.profileimage);
            Picasso.get().load(pfimage).into(imageView);
        }
        public void setlikecount(int k){

            String d=String.valueOf(k);
            likecount.setText(d+" "+"Likes");
        }

        public void setDislikecount(int i){
            String d=String.valueOf(i);
            dislikecount.setText(d+" "+"Dislikes");
        }

        public void setcommentcount(int j){
            System.out.println("dd"+j);
            String d=String.valueOf(j);
            commentcount.setText(d+" "+"Comments");
        }


    }




}
