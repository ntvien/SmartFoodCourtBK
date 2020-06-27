package com.example.smartfoodcourt;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPassword extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText editTextEmail;
    Button userPassword;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //final DatabaseReference table_user = database.getReference("User");

        toolbar = findViewById(R.id.toolbarForgotPassword);
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.editTextEmail);
        userPassword = findViewById(R.id.btnForgotPassWord);

        toolbar.setTitle("Forgot password");

//        FirebaseDatabase = FirebaseDatabase.getInstance();
//        final DatabaseReference table_user = database.getReference("User");
//
//        userPassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                progressBar.setVisibility(v.VISIBLE);
//                FirebaseDatabase.sendPasswordResetEmail(editTextEmail.getText().toString()).addOnCompleteListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(@NonNull Task<Void> task) {
//                        progressBar.setVisibility(v.GONE);
//                        if(task.isSuccessful()){
//                            Toast.makeText(ForgotPassword.this, "Password send to your email", Toast.LENGTH_LONG).show();
//                        }
//                        else{
//                            Toast.makeText(ForgotPassword.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            }
//        });
    }
}
