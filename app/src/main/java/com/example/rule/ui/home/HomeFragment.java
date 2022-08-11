package com.example.rule.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rule.EventActivity;
import com.example.rule.KoqActivity;
import com.example.rule.TaskActivity;
import com.example.rule.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements RecyclerViewInterface{

    ArrayList<ptModel> ptArrayList= new ArrayList<>();
    FloatingActionButton snap, floatingActionButton2;
    DatabaseReference database;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    String userid;
    ProgressDialog progressDialog;
    TextView tvNopt;

    private RecyclerView ptRecyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fStore =FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();
        snap= root.findViewById(R.id.floatingActionButton);
        floatingActionButton2=root.findViewById(R.id.floatingActionButton2);
        ptRecyclerView = root.findViewById(R.id.ptRecyclerView);
        tvNopt= root.findViewById(R.id.tv_nopt);
        ptRecyclerView.setHasFixedSize(true);
        ptRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        ptAdapter ptAdapter= new ptAdapter(root.getContext(),ptArrayList, this);
        ptRecyclerView.setAdapter(ptAdapter);

        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), KoqActivity.class);
                startActivity(intent);
            }
        });

        fetchPendingTask();
        return root;
    }


    private void fetchPendingTask() {
        userid= mAuth.getCurrentUser().getUid();
        fStore.collection("fsPendingTask").whereEqualTo("studentID",userid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.isEmpty()){
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    ptRecyclerView.setVisibility(View.GONE);
                    tvNopt.setVisibility(View.VISIBLE);
                }
                else {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    ptRecyclerView.setVisibility(View.VISIBLE);
                    tvNopt.setVisibility(View.GONE);
                }
                if (error != null){
                    Log.e("Firestore error", error.getMessage() );
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    if (dc.getType()==DocumentChange.Type.ADDED){
                        ptArrayList.add(dc.getDocument().toObject(ptModel.class));
                    }
                    if (dc.getType()==DocumentChange.Type.MODIFIED){
                        ptArrayList.add(dc.getDocument().toObject(ptModel.class));
                    }
                    if (dc.getType()==DocumentChange.Type.REMOVED){
                        ptArrayList.add(dc.getDocument().toObject(ptModel.class));
                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    ptRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }

        });
    }

    @Override
    public void onItemClick(int position) {

        //9data

            Intent intent= new Intent(getActivity(), TaskActivity.class);
        intent.putExtra("TASK", ptArrayList.get(position).getTask());
        intent.putExtra("STUDENT",ptArrayList.get(position).getStudentID());
        intent.putExtra("DOTG",ptArrayList.get(position).getDateOfTaskGiven());
        intent.putExtra("POT",ptArrayList.get(position).getPlace());
//        intent.putExtra("DOTU",ptArrayList.get(position).getDateOfTaskUploaded());
        intent.putExtra("DOCID",ptArrayList.get(position).getDocID());
        intent.putExtra("PICUID",ptArrayList.get(position).getPICuid());
        intent.putExtra("TASKDETAIL",ptArrayList.get(position).getTaskDetail());
        intent.putExtra("TYPE", ptArrayList.get(position).getType());
        intent.putExtra("RESULT", ptArrayList.get(position).getResult());
        startActivity(intent);
    }
}