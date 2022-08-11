package com.example.rule.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rule.R;

import java.util.ArrayList;

public class ptAdapter extends  RecyclerView.Adapter<ptAdapter.MyViewHolder>{

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<ptModel> ptArrayList;

    public ptAdapter(Context context, ArrayList<ptModel> ptArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.ptArrayList = ptArrayList;
        this.recyclerViewInterface= recyclerViewInterface;
    }

    @NonNull
    @Override
    public ptAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.frame_pending, parent, false);
        return new ptAdapter.MyViewHolder(view, recyclerViewInterface);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

//        ptModel ptModel= ptArrayList.get(position);
        holder.task.setText(ptArrayList.get(position).getTask());
        holder.tvDOTG.setText(ptArrayList.get(position).getDateOfTaskGiven());
        //holder.pic.setText(ptArrayList.get(position).getPic());

    }

    @Override
    public int getItemCount() {

        return ptArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView task;
        TextView tvDOTG;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            task = itemView.findViewById(R.id.tv_pendingTask);
            tvDOTG=itemView.findViewById(R.id.tv_DOTG);
            //pic = itemView.findViewById(R.id.tv_pic);
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
