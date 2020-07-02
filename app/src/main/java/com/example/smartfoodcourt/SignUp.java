package com.example.smartfoodcourt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText editUserName, editPassword, editEmail, editPhone;
    TextView textSignIn;
    Button btnSignUp;

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(editUserName.getText().toString()).exists()) {
                    mDialog.dismiss();
                    Toast.makeText(SignUp.this, "Username already register", Toast.LENGTH_LONG).show();
                } else {
                    mDialog.dismiss();
                    User user = new User(editEmail.getText().toString(), editPassword.getText().toString(), editPhone.getText().toString());
                    table_user.child(editUserName.getText().toString()).setValue(user);
                    Toast.makeText(SignUp.this, "Sign Up successfully !", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}