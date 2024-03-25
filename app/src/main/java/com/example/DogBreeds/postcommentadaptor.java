package com.example.DogBreeds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class postcommentadaptor extends RecyclerView.Adapter<postcommentadaptor.ViewHolder> {

   List<comment>commentlist;
   DatabaseReference db;
    String uname;
    String image;

    public postcommentadaptor(List<comment> commentlist) {
        this.commentlist=commentlist;
    }

    @NonNull
    @Override
    public postcommentadaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentpage,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postcommentadaptor.ViewHolder holder, int position) {
         String s=commentlist.get(position).uid;
        String text=commentlist.get(position).com;
        holder.setcomment(text);
        System.out.println(text);
         //holder.setuid(s);
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("users").child(s);
         db.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                  uname=snapshot.child("name").getValue().toString();
                 System.out.println(uname);
                  image=snapshot.child("image").getValue().toString();
                 holder.setusername(uname);
                 holder.setimage(image);

             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });


    }

    @Override
    public int getItemCount() {
        return commentlist.size();
    }


    public class ViewHolder  extends RecyclerView.ViewHolder{
        View mview;
        TextView commentusername,finalcomment;
        ImageView commentpfimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mview=itemView;
            commentusername=mview.findViewById(R.id.commentusername);
            commentpfimage=mview.findViewById(R.id.commnetpfimage);
            finalcomment=mview.findViewById(R.id.finalcomment);
        }

        public void setusername(String s) {
            commentusername.setText(s);
        }
        public void setimage(String d){
            Picasso.get().load(d).into(commentpfimage);
        }


        public void setcomment(String text) {
            finalcomment.setText(text);
        }
    }
}
