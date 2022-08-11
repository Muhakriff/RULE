package com.example.rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HistoryRequest extends AppCompatActivity {

    TextView etpe, etpic, etdot, etpot, etDescription;
    ImageView imgEvent;
    String task, pic, dotg, pot, docid, studentID, taskDetail, type,result, dotu;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_request);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Detail");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);
        etpe= findViewById(R.id.et_pe);
        etpic= findViewById(R.id.et_pic);
        etdot= findViewById(R.id.et_dot);
        etpot= findViewById(R.id.et_pot);
        etDescription= findViewById(R.id.et_description);
        imgEvent= findViewById(R.id.img_event);

        fStore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();

        task = getIntent().getStringExtra("TASK");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        docid = getIntent().getStringExtra("DOCID");
        studentID = getIntent().getStringExtra("STUDENT");
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
//        dotu = getIntent().getStringExtra("DOTU");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        etpe.setText(task);
        fStore.collection("users").document(pic).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                etpic.setText(value.getString("name"));
            }
        });
        etdot.setText(dotg);
        etpot.setText(pot);
        etDescription.setText(taskDetail);
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