package com.example.findmyband;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registration extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmationPassword;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Registration
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if( user != null ){
                    Intent intent = new Intent(registration.this, setup.class);
                    startActivity( intent );
                    finish();
                    return;
                }

            }
        };

        mRegister = (Button)findViewById(R.id.registerBtn);

        mEmail = (EditText)findViewById(R.id.emailText);
        mPassword = (EditText)findViewById(R.id.passwordText);
        mConfirmationPassword = (EditText)findViewById(R.id.confirmation);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mConfirmationPassword.getText().toString();

                if( !password.equals(confirmPassword) ){
                    Toast.makeText(registration.this, "Password and Confirmation Does not Match", Toast.LENGTH_SHORT).show();
                }

               else if ( (password.length() < 8 ) ) {
                    Toast.makeText(registration.this, "Enter a Password having 8 characters", Toast.LENGTH_SHORT).show();

                }
                else{

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(registration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(registration.this, "sign up error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            }
        });

        //Registration
    }

    //Registration
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
    //Registration
}