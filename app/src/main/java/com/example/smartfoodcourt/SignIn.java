package com.example.smartfoodcourt;

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

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    TextView textSignUp, textForgotPassword;
    Button btnSignIn;
    EditText editPassword, editUserName;
    CheckBox checkBoxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Remember Password
        rememberPassword();

        textSignUp = (TextView)findViewById(R.id.text_sign_up);
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        textForgotPassword = (TextView)findViewById(R.id.textForgotPassword);
        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        editPassword = (EditText)findViewById(R.id.editTextPassword);
        editUserName = (EditText)findViewById(R.id.editTextUserName);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkBoxRemember.isChecked()){
                    Paper.book().write(Common.USER_KEY, editUserName.getText().toString());
                    Paper.book().write(Common.PASSWORD_KEY, editPassword.getText().toString());
                }
                login();
            }
        });
    }

    //Login
    private void login() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User/List");

        final String username = editUserName.getText().toString();
        final String password = editPassword.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(SignIn.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(SignIn.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                mDialog.dismiss();
                if (snapshot.child(editUserName.getText().toString()).exists()) {
                    User user = snapshot.child(editUserName.getText().toString()).getValue(User.class);
                    if(user.getPassword().equals(editPassword.getText().toString())){
                        Intent homePageIntent = new Intent(getApplicationContext(), Home.class);
                        Common.user = user;
                        Common.userName = editUserName.getText().toString();
                        startActivity(homePageIntent);
                        finish();
                    }
                    else Toast.makeText(SignIn.this,"Wrong Password !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SignIn.this,"User not exist in DataBase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    //Remember Password
    private void checkRememberPassword(final String username, final String password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User/List");

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mDialog.dismiss();
                if (snapshot.child(username).exists()) {
                    User user = snapshot.child(username).getValue(User.class);
                    if (user.getPassword().equals(password)) {
                        Intent homePageIntent = new Intent(getApplicationContext(), Home.class);
                        Common.user = user;
                        Common.userName = editUserName.getText().toString();
                        startActivity(homePageIntent);
                        finish();
                    } else
                        Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void rememberPassword() {
        checkBoxRemember = (CheckBox)findViewById(R.id.checkBoxRemember);
        Paper.init(this);

        String user = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.PASSWORD_KEY);

        if(user != null && password != null){
            if(!user.isEmpty() && !password.isEmpty())
                checkRememberPassword(user, password);
        }
    }


}

