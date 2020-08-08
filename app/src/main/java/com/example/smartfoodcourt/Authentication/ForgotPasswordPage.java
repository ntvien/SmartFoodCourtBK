package com.example.smartfoodcourt.Authentication;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.smartfoodcourt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.VISIBLE;

public class ForgotPasswordPage extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progressBar;
    EditText editTextEmail;
    Button btnSendEmail;
    FirebaseDatabase database;
    DatabaseReference table_user;
    FirebaseAuth firebaseAuth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar)findViewById(R.id.toolbarForgotPassword);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        btnSendEmail = (Button)findViewById(R.id.btnSendEmail);

        toolbar.setTitle("Quên mật khẩu???");
        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User/List");
        firebaseAuth = FirebaseAuth.getInstance();

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail() {
        String email = editTextEmail.getText().toString();
        if(email.isEmpty()) return;
        progressBar.setVisibility(VISIBLE);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswordPage.this, "Vui lòng kiểm tra email để đặt lại mật khẩu", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPasswordPage.this, "Có gì đó không đúng!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
