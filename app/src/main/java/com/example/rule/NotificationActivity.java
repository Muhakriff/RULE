package com.example.rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class NotificationActivity extends AppCompatActivity {

    String task, studentID, dotg, pot, dotu, docid, pic, taskDetail, type, result ;
    ImageView imgEvent;
    TextView etpe, etpic, etdot, etpot, etDescription;
    FirebaseFirestore fStore;
    Button btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Poster Detail");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
//        dotu = getIntent().getStringExtra("DOTU");
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");

        etpe=findViewById(R.id.et_pe);
        etpic=findViewById(R.id.et_pic);
        etdot=findViewById(R.id.et_dot);
        etpot=findViewById(R.id.et_pot);
        etDescription=findViewById(R.id.et_description);
        imgEvent=findViewById(R.id.img_event);
        fStore=FirebaseFirestore.getInstance();
//        btnJoin=findViewById(R.id.btn_join);

        fStore.collection("users").document(pic).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                etpic.setText(value.getString("name"));
            }
        });
        etpe.setText(task);
        etdot.setText(dotg);
        etpot.setText(pot);
        etDescription.setText(taskDetail);

//        btnJoin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(NotificationActivity.this, EventActivity.class);
//
//                intent.putExtra("TASK", task);
////                intent.putExtra("STUDENT",studentID);
//                intent.putExtra("DOTG",dotg);
//                intent.putExtra("POT",pot);
////        intent.putExtra("DOTU",posterArrayList.get(position).getDateOfTaskUploaded());
////                intent.putExtra("DOCID",posterArrayList.get(position).getDocID());
//                intent.putExtra("PICUID",pic);
//                intent.putExtra("TASKDETAIL",taskDetail);
////                intent.putExtra("TYPE", posterArrayList.get(position).getType());
////                intent.putExtra("RESULT", posterArrayList.get(position).getResult());
//
//                startActivity(intent);
//
//            }
//        });
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