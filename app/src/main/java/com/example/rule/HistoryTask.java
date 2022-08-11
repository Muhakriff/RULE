package com.example.rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HistoryTask extends AppCompatActivity {

    TextView tvTask, tvPIC, tvDOT, tvPOT, tvNoteToStudent, tvDOC;
    ImageView imgDOC;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    String task, pic, dotg, pot, docid, studentID, taskDetail, type,result, dotu;
    StorageReference storageReference, taskRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Detail");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        task = getIntent().getStringExtra("TASK");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        docid = getIntent().getStringExtra("DOCID");
        studentID = getIntent().getStringExtra("STUDENT");
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        dotu = getIntent().getStringExtra("DOTU");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        imgDOC= findViewById(R.id.img_DOC);
        tvTask= findViewById(R.id.tv_Task);
        tvPIC=findViewById(R.id.tv_PIC);
        tvDOT=findViewById(R.id.tv_DOT);
        tvPOT=findViewById(R.id.tv_POT);
        tvNoteToStudent=findViewById(R.id.tv_noteToStudent);
        tvDOC=findViewById(R.id.tv_DOC);
        taskRef=storageReference.child("task/"+docid+".jpg");

        taskRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().into(imgDOC);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        tvTask.setText(task);
        fStore.collection("users").document(pic).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPIC.setText(value.getString("name"));
            }
        });
        tvDOT.setText(dotg);
        tvPOT.setText(pot);
        tvNoteToStudent.setText(taskDetail);
        tvDOC.setText(dotu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}