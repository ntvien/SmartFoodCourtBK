package com.example.smartfoodcourt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.User;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText editUserName, editPassword, editEmail, editPhone;
    TextView textSignIn;
    Button btnSignUp;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUserName = (EditText)findViewById(R.id.editTextUserName);
        editPhone = (EditText)findViewById(R.id.editTextPhone);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editPassword = (EditText)findViewById(R.id.editTextPassword);

        textSignIn = findViewById(R.id.text_sign_in);
        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp1);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {

        mAuth = FirebaseAuth.getInstance();

        final String email = editEmail.getText().toString();
        final String username = editUserName.getText().toString();
        final String password = editPassword.getText().toString();
        final String phone = editPhone.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(SignUp.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(SignUp.this, "Please Enter Phone", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUp.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUp.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(password.length() <= 6){
            Toast.makeText(SignUp.this, "Password too short", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(phone.length() != 10){
            Toast.makeText(SignUp.this,"Phone is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    User user = new User(email, password, phone);
                    FirebaseDatabase.getInstance().getReference("User").child(editUserName.getText().toString())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Sign up Successful", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Email existed or invalid. Sign up Failed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}