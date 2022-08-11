package com.example.rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class KoqHistory extends AppCompatActivity {

    TextView etComp, etPE,tvPIC, tvJawatan, tvPenglibatan,
            tvKomitmen, tvKS, tvKehadiran, tvPencapaian, tvTotal;
    String task, jawatan, kehadiran, ks, komitmen, picuid, pencapaian, penglibatan, docid, result, studentid, type, totalpoint, component;
    FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koq_history);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("History Detail");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        fStore=FirebaseFirestore.getInstance();

        etComp=findViewById(R.id.et_comp);
        etPE=findViewById(R.id.et_pe);
        tvPIC=findViewById(R.id.tv_PIC);
        tvJawatan=findViewById(R.id.tv_jawatan);
        tvPenglibatan=findViewById(R.id.tv_penglibatan);
        tvKomitmen=findViewById(R.id.tv_komitmen);
        tvKS=findViewById(R.id.tv_ks);
        tvKehadiran=findViewById(R.id.tv_kehadiran);
        tvPencapaian=findViewById(R.id.tv_pencapaian);
        tvTotal=findViewById(R.id.tv_total);

        task=getIntent().getStringExtra("TASK");
        jawatan=getIntent().getStringExtra("JAWATAN");
        kehadiran=getIntent().getStringExtra("KEHADIRAN");
        ks=getIntent().getStringExtra("KHIDMATSUMBANGAN");
        komitmen=getIntent().getStringExtra("KOMITMEN");
        picuid=getIntent().getStringExtra("PICUID");
        pencapaian=getIntent().getStringExtra("PENCAPAIAN");
        penglibatan=getIntent().getStringExtra("PENGLIBATAN");
        docid=getIntent().getStringExtra("DOCID");
        result=getIntent().getStringExtra("RESULT");
        studentid=getIntent().getStringExtra("STUDENTID");
        type=getIntent().getStringExtra("TYPE");
        totalpoint=getIntent().getStringExtra("TOTALPOINT");
        component=getIntent().getStringExtra("COMPONENT");


        etComp.setText(component);
        etPE.setText(task);
        fStore.collection("users").document(picuid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPIC.setText(value.getString("name"));
            }
        });
        fStore.collection("fsKokuSukan").document(jawatan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvJawatan.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(kehadiran).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKehadiran.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(ks).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKS.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(komitmen).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvKomitmen.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(pencapaian).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPencapaian.setText(value.getString("perkara"));
            }
        });
        fStore.collection("fsKokuSukan").document(penglibatan).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPenglibatan.setText(value.getString("perkara"));
            }
        });
        tvTotal.setText(totalpoint);
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