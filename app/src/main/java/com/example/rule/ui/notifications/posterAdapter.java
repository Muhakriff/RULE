package com.example.rule.ui.notifications;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rule.R;
import com.example.rule.ui.home.RecyclerViewInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class posterAdapter extends RecyclerView.Adapter<posterAdapter.MyViewHolder> {
    Context context;
    ArrayList<posterModel> posterArrayList;
    private final RecyclerViewInterface recyclerViewInterface;
    FirebaseFirestore fStore;
    StorageReference storageReference, storagePath;

    public posterAdapter(Context context, ArrayList<posterModel> posterArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.posterArrayList = posterArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public posterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_notification, parent, false);

        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        return new posterAdapter.MyViewHolder(view, recyclerViewInterface);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.imgPoster.setImageResource(posterArrayList.get(position).getPosterID());
        holder.tvEvent.setText(posterArrayList.get(position).getTask());
//    posterModel posterModel= posterArrayList.get(position);
//    String imgUri= null;
////    imgUri= posterModel.getPosterID();
//        Picasso.get().load(imgUri).fit().into(holder.imgPoster);
        holder.tvDateEvent.setText(posterArrayList.get(position).getDateOfTaskGiven());

        fStore.collection("users").document(posterArrayList.get(position).getPICuid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.tvOrganiser.setText(value.getString("name"));
            }
        });
        storagePath=storageReference.child("poster/"+posterArrayList.get(position).getDocID()+".jpg");

        storagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(holder.imgPoster);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return posterArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvEvent, tvDateEvent, tvOrganiser;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imgPoster= itemView.findViewById(R.id.img_poster);
            tvEvent= itemView.findViewById(R.id.tv_event);
            tvDateEvent= itemView.findViewById(R.id.tv_dateEvent);
            tvOrganiser=itemView.findViewById(R.id.tv_Organiser);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos= getBindingAdapterPosition();

                        if (pos!= RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
