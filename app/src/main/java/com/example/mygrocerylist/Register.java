package com.example.mygrocerylist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private TextView alreadyHaveAnAccount;
    private FrameLayout parentFrameLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;


    private ImageButton signUpBtn;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        signUpBtn = (ImageButton) findViewById(R.id.button_register);

        email = (EditText) findViewById(R.id.register_email);
        fullname = (EditText) findViewById(R.id.register_name);
        password = (EditText) findViewById(R.id.register_password);
        confirmpassword = (EditText) findViewById(R.id.register_cnfrmpass);

        alreadyHaveAnAccount = (TextView) findViewById(R.id.already_hav_register);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }


            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo send data to firebase
                checkEmailAndPassword();

            }


        });
    }


    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {
            if (!TextUtils.isEmpty(fullname.getText())) {
                if (!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirmpassword.getText())) {
                        signUpBtn.setEnabled(true);



                    } else {

                        signUpBtn.setEnabled(false);
                         }


                } else {
                    signUpBtn.setEnabled(false);
                       }

            } else {
                signUpBtn.setEnabled(false);

            }

        } else {
            signUpBtn.setEnabled(false);

        }

    }

    private void checkEmailAndPassword() {


        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (email.getText().toString().matches(emailPattern)) {
            if (password.getText().toString().equals(confirmpassword.getText().toString())) {


                signUpBtn.setEnabled(false);



                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<Object,String> userdata =new HashMap<>();
                                    userdata.put("fullname",fullname.getText().toString());

                                    firebaseFirestore.collection("USERS")
                                            .add(userdata)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        mainIntent();
                                                    }
                                                    else{

                                                        signUpBtn.setEnabled(true);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(Register.this,error, Toast.LENGTH_SHORT).show();


                                                    }
                                                }
                                            });


                                } else {

                                    signUpBtn.setEnabled(true);

                                    String error = task.getException().getMessage();
                                    Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                {

                }
            } else {
                confirmpassword.setError("password doesn't matched");
            }

        } else {
            email.setError("Invalid Email!");
        }


    }
    private void mainIntent(){
        Intent mainIntent = new Intent(Register.this, Login.class);
        startActivity(mainIntent);


    }



    }