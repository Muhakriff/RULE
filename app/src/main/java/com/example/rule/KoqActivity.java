package com.example.rule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KoqActivity extends AppCompatActivity {

    EditText etpe;
    RadioGroup groupRadioBtnComp;
    RadioButton radioCompBtn1;
    Button btnSend;
    TextView tvPointJawatan, tvPointPenglibatan, tvPointKomitmen, tvPointKS, tvPointKehadiran,tvPointPencapaian, tvTotal;
    FirebaseFirestore fStore;
    ArrayAdapter<Mark> jawatanAdapter, penglibatanAdapter, komitmenAdapter, ksAdapter, kehadiranAdapter, pencapaianAdapter;
    ArrayAdapter<User> picAdapter;
    AutoCompleteTextView itemJawatan, itemPenglibatan,itemKomitmen, itemKs, itemKehadiran, itemPencapaian, itemPic;
    Mark item;
    User temp;
    ArrayList<Mark> jawatanAL, penglibatanAL, komitmenAL, ksAL, kehadiranAL, pencapaianAL;
    ArrayList<User> picAL;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    String drop1, drop2, drop3, drop4, drop5, drop6, activity, docid, userid, pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koq);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Co-curriculum Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);
//        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
//        upArrow.setColorFilter(getResources().getColor(), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        etpe=findViewById(R.id.et_pe);
        btnSend=findViewById(R.id.btn_send);
        itemJawatan=findViewById(R.id.item_jawatan);
        itemPenglibatan=findViewById(R.id.item_penglibatan);
        itemKomitmen=findViewById(R.id.item_komitmen);
        itemKs=findViewById(R.id.item_ks);
        itemKehadiran=findViewById(R.id.item_kehadiran);
        itemPencapaian=findViewById(R.id.item_pencapaian);
        itemPic=findViewById(R.id.item_pic);
        tvPointJawatan=findViewById(R.id.tv_pointJawatan);
        tvPointPenglibatan=findViewById(R.id.tv_pointPenglibatan);
        tvPointKomitmen=findViewById(R.id.tv_pointkomitmen);
        tvPointKS=findViewById(R.id.tv_pointKS);
        tvPointKehadiran=findViewById(R.id.tv_pointKehadiran);
        tvPointPencapaian=findViewById(R.id.tv_pointPencapaian);
        tvTotal=findViewById(R.id.tv_total);
        groupRadioBtnComp=findViewById(R.id.radio_btnComp);

        drop1="Tiada";
        drop2="Tiada";
        drop3="Tiada";
        drop4="Tiada";
        drop5="Tiada";
        drop6="Tiada";

        fStore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        jawatanAL=new ArrayList<>();
        penglibatanAL=new ArrayList<>();
        komitmenAL=new ArrayList<>();
        ksAL=new ArrayList<>();
        kehadiranAL=new ArrayList<>();
        pencapaianAL=new ArrayList<>();
        picAL=new ArrayList<>();

        picAdapter=new ArrayAdapter<User>(this,R.layout.drop_down_item, picAL);
        picAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemPic.setAdapter(picAdapter);

        float total=0;

        fStore.collection("users").whereEqualTo("status", "teacher").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    User a=new User("null", "null", "null", "null", "null", "null",0);
                    picAL.add(a);
                }
                if(error!=null){
                    User a= new User("null", "null", "null", "null", "null", "null",0);
                    picAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        picAL.add(dc.getDocument().toObject(User.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        picAL.add(dc.getDocument().toObject(User.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        picAL.add(dc.getDocument().toObject(User.class));
                    }

                }
                picAdapter.notifyDataSetChanged();
            }
        });
        itemPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                temp=picAdapter.getItem(i);
                pic=temp.getDocID();
                itemPic.setError(null);
                itemPic.setKeyListener(null);
            }
        });

        jawatanAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, jawatanAL);
        jawatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemJawatan.setAdapter(jawatanAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Jawatan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    jawatanAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    jawatanAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        jawatanAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        jawatanAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        jawatanAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                jawatanAdapter.notifyDataSetChanged();
            }
        });
        itemJawatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=jawatanAdapter.getItem(i);
                drop1=item.getDocID();
                tvPointJawatan.setText(String.valueOf(item.getSkor()));
                itemJawatan.setKeyListener(null);
                calcPoint();
            }
        });


        penglibatanAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, penglibatanAL);
        penglibatanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemPenglibatan.setAdapter(penglibatanAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Penglibatan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    penglibatanAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    penglibatanAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        penglibatanAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        penglibatanAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        penglibatanAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                penglibatanAdapter.notifyDataSetChanged();
            }
        });
        itemPenglibatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=penglibatanAdapter.getItem(i);
                drop2=item.getDocID();
                tvPointPenglibatan.setText(String.valueOf(item.getSkor()));
                itemPenglibatan.setKeyListener(null);
                calcPoint();
            }
        });

        komitmenAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, komitmenAL);
        komitmenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemKomitmen.setAdapter(komitmenAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Komitmen").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    komitmenAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    komitmenAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        komitmenAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        komitmenAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        komitmenAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                komitmenAdapter.notifyDataSetChanged();
            }
        });
        itemKomitmen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=komitmenAdapter.getItem(i);
                drop3=item.getDocID();
                tvPointKomitmen.setText(String.valueOf(item.getSkor()));
                itemKomitmen.setKeyListener(null);
                calcPoint();

            }
        });

        ksAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, ksAL);
        ksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemKs.setAdapter(ksAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Khidmat Sumbangan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    ksAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    ksAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        ksAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        ksAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        ksAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                ksAdapter.notifyDataSetChanged();
            }
        });
        itemKs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=ksAdapter.getItem(i);
                drop4=item.getDocID();
                tvPointKS.setText(String.valueOf(item.getSkor()));
                itemKs.setKeyListener(null);
                calcPoint();
            }
        });

        kehadiranAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, kehadiranAL);
        kehadiranAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemKehadiran.setAdapter(kehadiranAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Kehadiran").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    kehadiranAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    kehadiranAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        kehadiranAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        kehadiranAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        kehadiranAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                kehadiranAdapter.notifyDataSetChanged();
            }
        });
        itemKehadiran.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=kehadiranAdapter.getItem(i);
                drop5=item.getDocID();
                tvPointKehadiran.setText(String.valueOf(item.getSkor()));
                itemKehadiran.setKeyListener(null);

                calcPoint();
            }
        });

        pencapaianAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, pencapaianAL);
        pencapaianAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemPencapaian.setAdapter(pencapaianAdapter);

        fStore.collection("fsKokuSukan").whereEqualTo("elemen", "Pencapaian").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    pencapaianAL.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    pencapaianAL.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        pencapaianAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        pencapaianAL.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        pencapaianAL.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                pencapaianAdapter.notifyDataSetChanged();
            }
        });
        itemPencapaian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item=pencapaianAdapter.getItem(i);
                drop6=item.getDocID();
                tvPointPencapaian.setText(String.valueOf(item.getSkor()));
                itemPencapaian.setKeyListener(null);
                calcPoint();
            }

        });


        tvTotal.setText(String.valueOf(total));

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadKoQ();
            }
        });
    }

    private void calcPoint() {
        double temp1=Double.parseDouble(tvPointJawatan.getText().toString());
        double temp2=Double.parseDouble(tvPointPenglibatan.getText().toString());
        double temp3=Double.parseDouble(tvPointKomitmen.getText().toString());
        double temp4=Double.parseDouble(tvPointKS.getText().toString());
        double temp5=Double.parseDouble(tvPointKehadiran.getText().toString());
        double temp6=Double.parseDouble(tvPointPencapaian.getText().toString());
        double total=0;

        total=temp1+temp2+temp3+temp4+temp5+temp6;
        tvTotal.setText(String.valueOf(total));
    }

    private void uploadKoQ() {

        int selectedID1=groupRadioBtnComp.getCheckedRadioButtonId();
        radioCompBtn1=findViewById(selectedID1);

        String comp= String.valueOf(radioCompBtn1.getText());

        activity=etpe.getText().toString().trim();
        if(activity==null||activity.isEmpty()){
            etpe.setError("This is required");
            etpe.requestFocus();
            return;
        }
        if(pic==null||pic.isEmpty()){
            itemPic.setError("This is required");
            itemPic.requestFocus();
            return;
        }
        userid=mAuth.getCurrentUser().getUid();

        docid=fStore.collection("fsPendingApproval").document().getId();
        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", activity);
        uploadTask.put("PICuid", pic);
        uploadTask.put("studentID", userid);
        uploadTask.put("jawatan", drop1);
        uploadTask.put("penglibatan", drop2);
        uploadTask.put("komitmen", drop3);
        uploadTask.put("khidmatSumbangan", drop4);
        uploadTask.put("kehadiran", drop5);
        uploadTask.put("pencapaian", drop6);
        uploadTask.put("docID", docid);
        uploadTask.put("type", "KoQ");
        uploadTask.put("component", comp);
        uploadTask.put("result", "Pending Approval");
        uploadTask.put("totalPoint", tvTotal.getText().toString());

        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been uploaded with id "+docid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "On Failure: "+e.toString());
            }
        });

        fStore.collection("fsPendingApproval").document(docid)
                .set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Task has been uploaded successfully by "+userid);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
        Toast.makeText(KoqActivity.this, "Data has been submitted for approval", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(KoqActivity.this, MainActivity.class);
        startActivity(intent);
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