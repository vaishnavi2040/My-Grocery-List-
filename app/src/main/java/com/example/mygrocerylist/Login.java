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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private TextView dontHaveAnAccount;
    private FrameLayout parentFrameLayout;
    private EditText email;
    private EditText password;

    private ImageButton signInBtn;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView forgotpassword;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dontHaveAnAccount = (TextView)findViewById(R.id.don_t_have_login);

        email = (EditText)findViewById(R.id.email_login);
        password = (EditText)findViewById(R.id.password_login);
        signInBtn=(ImageButton) findViewById(R.id.signin_button);
        forgotpassword=(TextView)findViewById(R.id.forgotPassword_login);

        firebaseAuth = FirebaseAuth.getInstance();
        dontHaveAnAccount=(TextView) findViewById(R.id.don_t_have_login);
        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));


            }
        });


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  onResetPasswordFragment=true;
                //setFragment(new ForgotPassword());

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
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }


        });
    }




    private void checkInputs() {
        if (!TextUtils.isEmpty(email.getText())) {

            if (!TextUtils.isEmpty(password.getText())) {

                signInBtn.setEnabled(true);



            } else {
                signInBtn.setEnabled(false);

                 }

        } else {
            signInBtn.setEnabled(false);
            email.setError("email is empty");
             }

    }
    private void checkEmailAndPassword() {
         if (email.getText().toString().trim().matches(emailPattern)) {
            if (password.length()>=8) {


                signInBtn.setEnabled(false);


                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    mainIntent();
                                }
                                else{
                                    String error = Objects.requireNonNull(task.getException()).getMessage();
                                    Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
                                     signInBtn.setEnabled(true);


                                }
                            }
                        });


            }
            else {
                password.setError("password less than 8  character");
            }

        } else {
            email.setError("Invalid Email!");
        }


    }
    private void mainIntent()
    {
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        startActivity(mainIntent);


    }
}