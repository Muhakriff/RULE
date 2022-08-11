package com.example.rule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

private Button btn_login;
private TextView register;
private TextInputEditText userid, password;

private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        btn_login= (Button)findViewById(R.id.btn_login);
        userid= (TextInputEditText) findViewById(R.id.Email);
        password=(TextInputEditText) findViewById(R.id.password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userlogin();
            }
        });

        mAuth= FirebaseAuth.getInstance();
        register= (TextView) findViewById(R.id.tv_register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register= new Intent(LoginActivity.this, RegisterUser.class);
                startActivity(register);
            }
        });

    }

    private void userlogin() {
        String strUserid= userid.getText().toString().trim();
        String strPassword= password.getText().toString().trim();

        if(strUserid.isEmpty()){
            userid.setError("Email is required.");
            userid.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(strUserid).matches()){
            userid.setError("Please provide valid email.");
            userid.requestFocus();
            return;
        }
        if(strPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(strPassword.length()<6){
            password.setError("Password must be at least 6 character");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(strUserid,strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Failed to login! Please check your email or password", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
