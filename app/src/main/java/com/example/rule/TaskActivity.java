package com.example.rule;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    //HomeFragment
    private TextView tvTask, tvPIC, tvDOT, tvPOT, tvDOC, tvNoteToStudent;
    Button btnUploadTask;
    ImageView imgDOC;
    String task, studentID, dotg, pot, dotu, docid, pic, taskDetail, type, result, uriUpload ;
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public static final String TAG = "TAG";
    private static final int REQUEST_CODE = 1;
    FloatingActionButton floatingActionButton4;
    ActivityResultLauncher<Intent> mGetContent;
    StorageReference storageReference, taskRef;
    Uri cam_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Task Form");
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


        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        taskRef= storageReference.child("task/"+docid+".jpg");


        Calendar calendar = Calendar.getInstance();
        dotu = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        tvTask= findViewById(R.id.tv_Task);
        tvPIC= findViewById(R.id.tv_PIC);
        tvDOT= findViewById(R.id.tv_DOT);
        tvPOT= findViewById(R.id.tv_POT);
        tvDOC = findViewById(R.id.tv_DOC);
        tvNoteToStudent= findViewById(R.id.tv_noteToStudent);
        imgDOC= findViewById(R.id.img_DOC);
        btnUploadTask= findViewById(R.id.btn_upload_Task);
        floatingActionButton4=findViewById(R.id.floatingActionButton4);

        fStore.collection("users").document(pic).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                tvPIC.setText(value.getString("name"));
            }
        });
        tvNoteToStudent.setText(taskDetail);
        tvTask.setText(task);
//        tvPIC.setText(pic);
        tvDOT.setText(dotg);
        tvPOT.setText(pot);
        tvDOC.setText(dotu);

        btnUploadTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadTask();
            }
        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPermissions();
                pickCamera();


            }
        });
    }

    private void verifyPermissions() {
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
            ActivityCompat.requestPermissions(TaskActivity.this,permissions, REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        verifyPermissions();
    }

    private void uploadTask() {

        task = getIntent().getStringExtra("TASK");
        studentID = getIntent().getStringExtra("STUDENT");
        dotg = getIntent().getStringExtra("DOTG");
        pot = getIntent().getStringExtra("POT");
        dotu= tvDOC.getText().toString().trim();
        docid = getIntent().getStringExtra("DOCID");
        pic = getIntent().getStringExtra("PICUID"); //namacikgu
        taskDetail = getIntent().getStringExtra("TASKDETAIL");
        type = getIntent().getStringExtra("TYPE");
        result = getIntent().getStringExtra("RESULT");


        Map<String, Object> uploadTask= new HashMap<>();
        uploadTask.put("task", task);
        uploadTask.put("studentID",studentID);
        uploadTask.put("DateOfTaskGiven", dotg);
        uploadTask.put("Place", pot);
        uploadTask.put("DateOfTaskUploaded", dotu);
        uploadTask.put("docID", docid);
        uploadTask.put("PICuid", pic); //namacikgu
        uploadTask.put("taskDetail",taskDetail);
        uploadTask.put("type", type);
        uploadTask.put("result", "Pending Approval");
//        uploadTask.put("camURI", taskRef);
        if(imgDOC.getDrawable()==null){
            Toast.makeText(TaskActivity.this, "Please take a picture", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            taskRef.putFile(cam_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                    Toast.makeText(TaskActivity.this,"Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        }


        fStore.collection("fsPendingApproval").document(docid).set(uploadTask).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        fStore.collection("fsPendingTask").document(docid).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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

        Toast.makeText(TaskActivity.this,"Task submitted. Kindly check history for status.",Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(TaskActivity.this, MainActivity.class);
        startActivity(intent);
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
                        Picasso.get().load(cam_uri).fit().into(imgDOC);
                        uriUpload=cam_uri.toString();
//                        imgDOC.setImageURI(cam_uri);

                    }
                }
            });
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