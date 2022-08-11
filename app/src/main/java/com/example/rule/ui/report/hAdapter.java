package com.example.rule.ui.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rule.R;
import com.example.rule.ui.home.RecyclerViewInterface;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class hAdapter extends  RecyclerView.Adapter<hAdapter.MyViewHolder> {
    Context context;
    ArrayList<historyModel> historyArrayList;
    private final RecyclerViewInterface recyclerViewInterface;
    FirebaseFirestore fStore;

    public hAdapter(Context context, ArrayList<historyModel> historyArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.historyArrayList = historyArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public hAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_history, parent, false);
        fStore=FirebaseFirestore.getInstance();
        return new hAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull hAdapter.MyViewHolder holder, int position) {
        holder.tvHistory.setText(historyArrayList.get(position).getTask());
        fStore.collection("users").document(historyArrayList.get(position).getPICuid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                holder.tvOrganiser.setText(value.getString("name"));
            }
        });
        String type=historyArrayList.get(position).getType();
        String result=historyArrayList.get(position).getResult();
        switch (type){
            case "task":
                if(result.equals("Approved")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_task_24_green);
                }
                else if(result.equals("Rejected")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_task_24_red);
                }
                else if(result.equals("Pending Approval")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_task_24_yellow);
                }
                else if(result.equals("Pending Response")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_task_24_yellow);
                }
                break;
            case "event":
                if(result.equals("Approved")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_a_photo_24_green);
                }
                else if(result.equals("Rejected")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_a_photo_24_red);
                }
                else if(result.equals("Pending Approval")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_a_photo_24_yellow);
                }
                else if(result.equals("Pending Response")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_a_photo_24_yellow);
                }
                break;
            case "request":
                if(result.equals("Approved")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_celebration_24_green);
                }
                else if(result.equals("Rejected")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_celebration_24_red);
                }
                else if(result.equals("Pending Approval")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_celebration_24_yellow);
                }
                else if(result.equals("Pending Response")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_celebration_24_yellow);
                }
                break;
            case "KoQ":
                if(result.equals("Approved")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_24_green);
                }
                else if(result.equals("Rejected")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_24_red);
                }
                else if(result.equals("Pending Approval")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_24_yellow);
                }
                else if(result.equals("Pending Response")){
                    holder.imgIcon.setImageResource(R.drawable.ic_baseline_add_24_yellow);
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return historyArrayList.size();    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imgIcon;
        TextView tvHistory, tvOrganiser;
        ConstraintLayout frameBG;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            tvHistory = itemView.findViewById(R.id.tv_history);
            imgIcon=itemView.findViewById(R.id.img_icon);
            tvOrganiser=itemView.findViewById(R.id.tv_PIC);
            frameBG=itemView.findViewById(R.id.frame_bg);

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
