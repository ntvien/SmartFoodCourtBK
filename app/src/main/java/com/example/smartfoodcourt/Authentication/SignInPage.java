package com.example.smartfoodcourt.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.HomePage;
import com.example.smartfoodcourt.Model.User;
import com.example.smartfoodcourt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignInPage extends AppCompatActivity {

    TextView textSignUp, textForgotPassword;
    Button btnSignIn;
    EditText editPassword, editEmail;
    CheckBox checkBoxRemember;
    FirebaseAuth mAuth;
    DatabaseReference userReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        checkBoxRemember = (CheckBox)findViewById(R.id.checkBoxRemember);
        textSignUp = (TextView)findViewById(R.id.text_sign_up);
        textForgotPassword = (TextView)findViewById(R.id.textForgotPassword);
        editPassword = (EditText)findViewById(R.id.editTextPassword);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("User/List");
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(intent);
            }
        });

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInPage.this, ForgotPasswordPage.class);
                startActivity(intent);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
        Paper.init(this);
        checkSavedUser();
    }

    private void checkInput(){
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignInPage.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignInPage.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            signIn(email, password);
        }
    }

    private void signIn(final String email, final String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignInPage.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Common.user = snapshot.getValue(User.class);
                                    mDialog.dismiss();
                                    if(checkBoxRemember.isChecked()){
                                        Paper.book().write(Common.EMAIL_KEY, email);
                                        Paper.book().write(Common.PASSWORD_KEY, password);
                                    }
                                    Intent homePageIntent = new Intent(SignInPage.this, HomePage.class);
                                    startActivity(homePageIntent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(SignInPage.this, "Authentication failed. Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkSavedUser() {
        String email = Paper.book().read(Common.EMAIL_KEY);
        String password = Paper.book().read(Common.PASSWORD_KEY);
        if(email != null && password != null){
            signIn(email, password);
        }
    }


}

