package com.example.rule.ui.notifications;

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

import com.example.rule.NotificationActivity;
import com.example.rule.PosterActivity;
import com.example.rule.R;
import com.example.rule.ui.home.RecyclerViewInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements RecyclerViewInterface  {

    private RecyclerView posterRecyclerView;
    ArrayList<posterModel> posterArrayList= new ArrayList<>();
    FloatingActionButton floatingActionButton5;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userid;
    ProgressDialog progressDialog;
    TextView tvNopt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        fStore=FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        floatingActionButton5= root.findViewById(R.id.floatingActionButton5);
        tvNopt=root.findViewById(R.id.tv_nopt);

        posterRecyclerView = root.findViewById(R.id.notiRecyclerView);
        posterRecyclerView.setHasFixedSize(true);
        posterRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        posterAdapter posterAdapter= new posterAdapter(root.getContext(), posterArrayList, this::onItemClick);
        posterRecyclerView.setAdapter(posterAdapter);

        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PosterActivity.class);
                startActivity(intent);
            }
        });
        fetchPoster();
        return root;
    }

    private void fetchPoster() {
        fStore.collection("fsPosterEvent").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.isEmpty()){
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    posterRecyclerView.setVisibility(View.GONE);
                    tvNopt.setVisibility(View.VISIBLE);
                }
                else {
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    posterRecyclerView.setVisibility(View.VISIBLE);
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
                        posterArrayList.add(dc.getDocument().toObject(posterModel.class));
                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    posterRecyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        //9data
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        intent.putExtra("TASK", posterArrayList.get(position).getTask());
        intent.putExtra("STUDENT",posterArrayList.get(position).getStudentID());
        intent.putExtra("DOTG",posterArrayList.get(position).getDateOfTaskGiven());
        intent.putExtra("POT",posterArrayList.get(position).getPlace());
//        intent.putExtra("DOTU",posterArrayList.get(position).getDateOfTaskUploaded());
        intent.putExtra("DOCID",posterArrayList.get(position).getDocID());
        intent.putExtra("PICUID",posterArrayList.get(position).getPICuid());
        intent.putExtra("TASKDETAIL",posterArrayList.get(position).getTaskDetail());
        intent.putExtra("TYPE", posterArrayList.get(position).getType());
        intent.putExtra("RESULT", posterArrayList.get(position).getResult());
        startActivity(intent);


    }
}