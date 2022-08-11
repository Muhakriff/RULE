package com.example.rule;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PosterActivity extends AppCompatActivity {

    ImageView imgEvent;
    FloatingActionButton floatingActionButton2;
    EditText etpe, etpic, etdotg, etpot, etDescription;
    Button btnSend;
    FirebaseFirestore fStore;
    FirebaseAuth mAuth;
    User temp;
    ArrayAdapter<User> itemAdapter;
    ArrayList<User> picAL;
    AutoCompleteTextView filledExposed;
    public static final String TAG = "TAG";
    String nameEvent, personEvent, dateEvent, placeEvent, descriptionEvent, userid, docid;
    ActivityResultLauncher<String> mGetContent;
    StorageReference storageReference, posterRef;
    Uri posterURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Poster Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        imgEvent= findViewById(R.id.img_event);
        floatingActionButton2= findViewById(R.id.floatingActionButton2);
        etpe= findViewById(R.id.et_pe);
        etpot= findViewById(R.id.et_pot);
        etDescription= findViewById(R.id.et_description);
        btnSend= findViewById(R.id.btn_send);
        filledExposed= findViewById(R.id.filled_exposed);
        etdotg=findViewById(R.id.et_dotg);

        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        picAL=new ArrayList<>();
        itemAdapter=new ArrayAdapter<User>(this,R.layout.drop_down_item, picAL);
        itemAdapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(itemAdapter);
        filledExposed.setAdapter(itemAdapter);
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
                itemAdapter.notifyDataSetChanged();
            }
        });
        filledExposed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                temp=itemAdapter.getItem(i);
                personEvent=temp.getDocID();
                filledExposed.setError(null);
                filledExposed.setKeyListener(null);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestEvent();
            }
        });
        Calendar calendar = Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        etdotg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(PosterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1=i1+1;
                        String date= i2+"/"+i1+"/"+i;
                        etdotg.setText(date);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });

        mGetContent= registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //imgbtnProfile.setImageURI(result);
                if(result!= null){
                    Picasso.get().load(result).fit().into(imgEvent);
                    posterURL=result;
                }
                else{
                    Toast.makeText(PosterActivity.this, "No picture selected",Toast.LENGTH_SHORT).show();
                }

            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });
    }

    private void uploadPosterToFirebase(Uri result) {



    }


    private void requestEvent() { //9data

        nameEvent= etpe.getText().toString().trim();
        dateEvent= etdotg.getText().toString().trim();
        placeEvent= etpot.getText().toString().trim();
        descriptionEvent= etDescription.getText().toString().trim();

        if(nameEvent==null || nameEvent.isEmpty()){
            etpe.setError("This is required.");
            etpe.requestFocus();
            return;
        }
        if(personEvent==null || personEvent.isEmpty()){
            filledExposed.setError("This is required.");
            filledExposed.requestFocus();
            return;
        }
        if(personEvent!=null||!personEvent.isEmpty()){
            filledExposed.setError(null);
        }
        if(dateEvent==null || dateEvent.isEmpty()){
            etdotg.setError("This is required.");
            etdotg.requestFocus();
            return;
        }
        if(dateEvent!=null||!dateEvent.isEmpty()){
            etdotg.setError(null);
        }

        if(placeEvent==null || placeEvent.isEmpty()) {
            etpot.setError("This is required.");
            etpot.requestFocus();
            return;
        }
        if(descriptionEvent==null || descriptionEvent.isEmpty()) {
            etDescription.setError("This is required.");
            etDescription.requestFocus();
            return;
        }
        userid= mAuth.getCurrentUser().getUid();
        docid= fStore.collection("fsPendingApproval").document().getId();
        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", nameEvent);
        uploadTask.put("studentID", userid);
        uploadTask.put("PICuid", personEvent);
        uploadTask.put("DateOfTaskGiven", dateEvent);
        uploadTask.put("Place", placeEvent);
        uploadTask.put("taskDetail", descriptionEvent);
        uploadTask.put("docID", docid);
        uploadTask.put("type", "request");
        uploadTask.put("result", "Pending Approval");

        posterRef= storageReference.child("poster/"+docid+".jpg");

        if(imgEvent.getDrawable()==null){
            Toast.makeText(PosterActivity.this, "Please upload a picture", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            posterRef.putFile(posterURL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    posterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).fit().into(imgEvent);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PosterActivity.this,"Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }

            fStore.collection("fsPendingApproval").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Poster has been uploaded successfully by "+userid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
        fStore.collection("fsHistory").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: Poster has been uploaded successfully by "+userid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
        Toast.makeText(PosterActivity.this, "Poster submitted for approval", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(PosterActivity.this, MainActivity.class);
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