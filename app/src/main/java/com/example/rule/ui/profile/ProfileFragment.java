package com.example.rule.ui.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rule.LoginActivity;
import com.example.rule.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private Button btnLogout;
    private TextView etEmail, etName, etID, etPassword, etClassroom, tvStudGender, tvStudAge;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String dbID;
    ImageView imgbtnProfile;
    FloatingActionButton floatingActionButton;
    ActivityResultLauncher<String> mGetContent;
    StorageReference storageReference, profileRef;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        progressDialog= new ProgressDialog(root.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        btnLogout= root.findViewById(R.id.btnLogout);
        etEmail= root.findViewById(R.id.tv_profEmail);
        etName= root.findViewById(R.id.tv_profName);
        etID= root.findViewById(R.id.tv_studID);
        etClassroom= root.findViewById(R.id.tv_studClassroom);
        tvStudGender=root.findViewById(R.id.tv_studGender);
        tvStudAge=root.findViewById(R.id.tv_studAge);
        floatingActionButton= (FloatingActionButton) root.findViewById(R.id.floatingActionButton3);
        imgbtnProfile= (ImageView)root.findViewById(R.id.img_prof_pic);
        //imageView2= root.findViewById(R.id.imageView2);

        mAuth= FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        dbID= mAuth.getCurrentUser().getUid();
        storageReference= FirebaseStorage.getInstance().getReference();
        profileRef= storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");

        mGetContent= registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //imgbtnProfile.setImageURI(result);
                if(result!= null){
                    uploadImageToFirebase(result);
                }
                else{
                    Toast.makeText(getActivity(), "No picture selected",Toast.LENGTH_SHORT).show();
                }

            }
        });

        DocumentReference documentReference= fStore.collection("users").document(dbID);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    if(progressDialog.isShowing()) {
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).fit().into(imgbtnProfile);
                                progressDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                        etEmail.setText(value.getString("email"));
                        etName.setText(value.getString("name"));
                        etID.setText(value.getString("ID"));
                        etClassroom.setText(value.getString("classroom"));
                        tvStudGender.setText(value.getString("gender"));
                        if(value.getString("gender").equals("Male")){
                            tvStudGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_male_24,0,0,0);
                        }
                        else if(value.getString("gender").equals("Female")){
                            tvStudGender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_female_24,0,0,0);
                        }
                        tvStudAge.setText(value.getString("age"));
                        progressDialog.dismiss();
                    }

            }
        });
        btnLogout.setOnClickListener(this);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        return root;

    }

    private void uploadImageToFirebase(Uri result) {
        profileRef.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).fit().into(imgbtnProfile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));

    }
}