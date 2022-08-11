package com.example.rule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    public static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private EditText registerEmail, registerName, registerID, registerClassroom, registerPassword;
    FirebaseFirestore fStore;
    String dbID;
    RadioGroup radioBtnGroup, radioBtnGroup2;
    RadioButton radioSexBtn, radioSexBtn2;

    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registration Form");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        //getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        btnRegister= (Button)findViewById(R.id.btn_register);
        registerEmail= (EditText) findViewById(R.id.registerEmail);
        registerName= (EditText)findViewById(R.id.registerName);
        registerID= (EditText)findViewById(R.id.registerID);
        registerClassroom=(EditText)findViewById((R.id.registerClassroom));
        registerPassword= (EditText) findViewById(R.id.registerPassword);
        radioBtnGroup=(RadioGroup)findViewById(R.id.radio_btngroup);

        radioBtnGroup2=(RadioGroup)findViewById(R.id.radio_btngroup2);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
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

    private void registerUser() {

        int selectedID= radioBtnGroup.getCheckedRadioButtonId();
        radioSexBtn=findViewById(selectedID);
        int selectedID2= radioBtnGroup2.getCheckedRadioButtonId();
        radioSexBtn2=findViewById(selectedID2);
        String email= registerEmail.getText().toString().trim();
        String name= registerName.getText().toString().trim();
        String ID= registerID.getText().toString().trim();
        String classroom= registerClassroom.getText().toString().trim();
        String password= registerPassword.getText().toString().trim();
        String gender= String.valueOf(radioSexBtn.getText());
        String age= String.valueOf(radioSexBtn2.getText());
        if(email.isEmpty()){
            registerEmail.setError("Email is required.");
            registerEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Please provide valid email.");
            registerEmail.requestFocus();
            return;
        }
        if (name.isEmpty()){
            registerName.setError("Full name is required.");
            registerName.requestFocus();
            return;
        }
        if (ID.isEmpty()){
            registerID.setError("ID is required.");
            registerID.requestFocus();
            return;
        }
        if(password.isEmpty()){
            registerPassword.setError("Password is required");
            registerPassword.requestFocus();
            return;
        }
        if(password.length()<6){
            registerPassword.setError("Password must be at least 6 character");
            registerPassword.requestFocus();
            return;
        }
        if (classroom.isEmpty()){
            registerClassroom.setError("Classroom is required.");
            registerClassroom.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String temp = "student";
                    dbID= mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference= fStore.collection("users").document(dbID);
                    Map<String, Object> user= new HashMap<>();
                    user.put("email", email);
                    user.put("name", name);
                    user.put("ID", ID);
                    user.put("classroom", classroom);
                    user.put("status", temp);
                    user.put("docID", dbID);
                    user.put("gender", gender);
                    user.put("age", age);

                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: user profile is created for "+ dbID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: "+ e.toString());
                        }
                    });
                    Toast.makeText(RegisterUser.this,"User has been created",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();

                }else{
                    Toast.makeText(RegisterUser.this, "Unsuccessful Registration! "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}