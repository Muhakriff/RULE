package com.example.rule;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    ImageView imgEvent, imgHelp;
    FloatingActionButton floatingActionButton2;
    EditText etpe, etpic, etdotg,  etpot, etDescription;
    TextView etdotu, tvPoint;
    Button btnSend;
    FirebaseFirestore fStore;
    ArrayList<User> testUser= new ArrayList<>();
    ArrayList<Mark> EventName= new ArrayList<>();
    AutoCompleteTextView filledExposed, eventItem;
    ArrayAdapter<Mark> markAdapter;
    ArrayAdapter<User> itemAdapter;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    private static final int REQUEST_CODE = 1;
    User teacherID;
    Mark mark;
    StorageReference storageReference, storagePath;
    Uri cam_uri;
    String nameEvent, personEvent, dateEvent, placeEvent, descriptionEvent, userid, docid, dotu, dotg, task, pot, pic, taskDetail, temp, eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Extra Co-curriculum Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

//        task = getIntent().getStringExtra("TASK");
//        temp = getIntent().getStringExtra("DOTG");
//        pot = getIntent().getStringExtra("POT");
//        pic = getIntent().getStringExtra("PICUID"); //namacikgu
//        taskDetail = getIntent().getStringExtra("TASKDETAIL");

        imgEvent= findViewById(R.id.img_event);
        floatingActionButton2= findViewById(R.id.floatingActionButton2);
        etpe= findViewById(R.id.et_pe);
        etdotu= findViewById(R.id.et_dotu);
        etpot= findViewById(R.id.et_pot);
        etDescription= findViewById(R.id.et_description);
        btnSend= findViewById(R.id.btn_send);
        etdotg= findViewById(R.id.et_dotg);
        filledExposed=findViewById(R.id.filled_exposed);
        eventItem=findViewById(R.id.event_item);
        tvPoint=findViewById(R.id.tv_point);

        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();

        markAdapter=new ArrayAdapter<Mark>(this,R.layout.drop_down_item, EventName);
        markAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventItem.setAdapter(markAdapter);

        fStore.collection("fsExtraKokurikulum").whereEqualTo("elemen", "Perkhidmatan").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.isEmpty()){
                    Mark a=new Mark("null", "null", "null", "null", 0);
                    EventName.add(a);
                }
                if(error!=null){
                    Mark a= new Mark("null", "null", "null", "null", 0);
                    EventName.add(a);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        EventName.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        EventName.add(dc.getDocument().toObject(Mark.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        EventName.add(dc.getDocument().toObject(Mark.class));
                    }

                }
                markAdapter.notifyDataSetChanged();
            }
        });
        eventItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mark=markAdapter.getItem(i);
                eventID=mark.getDocID();
                tvPoint.setText(String.valueOf(mark.getSkor()));
                eventItem.setKeyListener(null);
            }
        });

        itemAdapter= new ArrayAdapter<User>(this, R.layout.drop_down_item, testUser);
        itemAdapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filledExposed.setAdapter(itemAdapter);
        fStore.collection("users").whereEqualTo("status","teacher").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.isEmpty()){
                    User a= new User("null", "null", "null", "null", "null", "null", 0);
                    testUser.add(a);
                }
                if(error!=null){
                    User a= new User("Error Firestore", "Error Firestore", "Error Firestore", "Error Firestore", "Error Firestore", "Error Firestore", 0);
                }
                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType()==DocumentChange.Type.ADDED){
                        testUser.add(dc.getDocument().toObject(User.class));
                    }
                    if(dc.getType()==DocumentChange.Type.MODIFIED){
                        testUser.add(dc.getDocument().toObject(User.class));
                    }
                    if(dc.getType()==DocumentChange.Type.REMOVED){
                        testUser.add(dc.getDocument().toObject(User.class));
                    }

                }
                itemAdapter.notifyDataSetChanged();
            }
        });
        filledExposed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                teacherID= itemAdapter.getItem(i);
                personEvent=teacherID.getDocID();
                eventItem.setKeyListener(null);

            }
        });
        Calendar calendar = Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions();
                pickCamera();
            }
        });

        etdotg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(EventActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        String currentDate= DateFormat.getDateInstance().format(calendar.getTime());
        currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        etdotu.setText(currentDate);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTask();
            }
        });

        if(task!=null||temp!=null||pot!=null||pic!=null||taskDetail!=null){
            etdotg.setText(temp);
            etpe.setText(task);
            etpot.setText(pot);

            fStore.collection("users").document(pic).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    filledExposed.setText(value.getString("name"));
                }
            });
            etDescription.setText(taskDetail);
        }else{
            return;
        }
        imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        cam_uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);

        //startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE); // OLD WAY
        startCamera.launch(cameraIntent);                // VERY NEW WAY


    }


    ActivityResultLauncher<Intent> startCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        Picasso.get().load(cam_uri).fit().into(imgEvent);

//                        imgEvent.setImageURI(cam_uri);
//                        imgEvent.setTag(cam_uri.toString());

                    }
                }
            });

    private void sendTask() { //11data

        dotg= etdotg.getText().toString().trim();
        nameEvent= etpe.getText().toString().trim();
        dateEvent= etdotu.getText().toString().trim();
        placeEvent= etpot.getText().toString().trim();
        descriptionEvent= etDescription.getText().toString().trim();
//        String path=imgEvent.getTag().toString();

//        if(path==null||path.isEmpty()){
//            floatingActionButton2.s
//        }
        userid= mAuth.getCurrentUser().getUid();
//        if(cam_uri!=null){
//            if(cam_uri.equals(Uri.EMPTY)){
//                Toast.makeText(EventActivity.this, "You have to put image", Toast.LENGTH_SHORT).show();
//                floatingActionButton2.setFocusable(true);
//                floatingActionButton2.setFocusableInTouchMode(true);///add this line
//                floatingActionButton2.requestFocus();
//                floatingActionButton2.requestFocus();
//                Toast.makeText(EventActivity.this, "cam uri equal empty", Toast.LENGTH_SHORT).show();
//            }
//        }
//        if(cam_uri==null){
//            Toast.makeText(EventActivity.this, "You have to put image", Toast.LENGTH_SHORT).show();
//            floatingActionButton2.setFocusable(true);
//            floatingActionButton2.setFocusableInTouchMode(true);///add this line
//            floatingActionButton2.requestFocus();
//            Toast.makeText(EventActivity.this, "cam uri null", Toast.LENGTH_SHORT).show();
//        }
        if(nameEvent==null || nameEvent.isEmpty()){
            etpe.setError("This is required.");
            etpe.requestFocus();
            return;
        }
        if(eventID==null||eventID.isEmpty()){
            eventItem.setError("This is required");
            eventItem.requestFocus();
            return;
        }
        if(eventID!=null||!eventID.isEmpty()){
            eventItem.setError(null);
        }
        if(personEvent==null || personEvent.isEmpty()){
            filledExposed.setError("This is required.");
            filledExposed.requestFocus();
            return;
        }
        if(personEvent!=null||!personEvent.isEmpty()){
            filledExposed.setError(null);
        }
        if(dotg==null || dotg.isEmpty()){
            etdotg.setError("This is required.");
            etdotg.requestFocus();
            return;
        }
        if(dotg!=null||!dotg.isEmpty()){
            etdotg.setError(null);
        }
        if(dateEvent==null || dateEvent.isEmpty()){
            etdotu.setError("This is required.");
            etdotu.requestFocus();
            return;
        }
        if(dateEvent!=null||!dateEvent.isEmpty()){
            etdotu.setError(null);
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
        String totalPoint= tvPoint.getText().toString().trim();
        docid= fStore.collection("fsPendingApproval").document().getId();
        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", nameEvent);
        uploadTask.put("studentID", userid);
        uploadTask.put("DateOfTaskUploaded", dateEvent);
        uploadTask.put("Place", placeEvent);
        uploadTask.put("DateOfTaskGiven", dotg);
        uploadTask.put("taskDetail", descriptionEvent);
        uploadTask.put("PICuid", personEvent);
        uploadTask.put("eventID", eventID);
        uploadTask.put("docID", docid);
        uploadTask.put("type", "event");
        uploadTask.put("result", "Pending Approval");
        uploadTask.put("totalPoint", totalPoint);
        storagePath = storageReference.child("xtraKoQ/"+docid+".jpg");
        if(imgEvent.getDrawable()==null){
            Toast.makeText(EventActivity.this, "Please take a picture", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            storagePath.putFile(cam_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadURL= uri;
                            uploadTask.put("camURI", downloadURL);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EventActivity.this,"Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }


//        storagePath= storageReference.child("Event/"+docid+".jpg");
//        storagePath.putFile(cam_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                storagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        uploadTask.put("picturePath", uri);
//                    }
//                });
//            }
//        });
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
        Toast.makeText(EventActivity.this, "Event has been submitted for approval", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(EventActivity.this, MainActivity.class);
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
    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");
        String [] permissions= {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0])== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1])== PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2])== PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(EventActivity.this,permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermissions();
    }
}