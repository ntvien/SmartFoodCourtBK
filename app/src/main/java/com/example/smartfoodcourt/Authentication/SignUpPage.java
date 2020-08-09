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

import com.example.smartfoodcourt.Model.User;
import com.example.smartfoodcourt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    EditText editUserName, editPassword, editEmail, editPhone;
    TextView textSignIn;
    Button btnSignUp;

    FirebaseAuth mAuth;
    DatabaseReference userReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference("User/List");
        editUserName = (EditText)findViewById(R.id.editTextUserName);
        editPhone = (EditText)findViewById(R.id.editTextPhone);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
        editPassword = (EditText)findViewById(R.id.editTextPassword);

        textSignIn = findViewById(R.id.text_sign_in);
        textSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpPage.this, SignInPage.class));
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

    private boolean checkInput(String email, String username, String password, String phone) {

        if(TextUtils.isEmpty(username)){
            Toast.makeText(SignUpPage.this, "Vui lòng nhập tên người dùng", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(SignUpPage.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(SignUpPage.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(SignUpPage.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length() < 6){
            Toast.makeText(SignUpPage.this, "Mật khẩu quá ngắn", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(phone.length() < 10){
            Toast.makeText(SignUpPage.this,"Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signUp() {
        final String email = editEmail.getText().toString();
        final String username = editUserName.getText().toString();
        String password = editPassword.getText().toString();
        final String phone = editPhone.getText().toString();
        if(checkInput(email, username, password, phone)){
            final ProgressDialog mDialog = new ProgressDialog(SignUpPage.this);
            mDialog.setMessage("Xin vui lòng đợi...");
            mDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User user = new User(email, username, phone);
                        userReference.child(mAuth.getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            mDialog.dismiss();
                                            Toast.makeText(SignUpPage.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                        }
                                    }
                                });
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(SignUpPage.this, "Email đã tồn tại hoặc không hợp lệ. Đăng ký không thành công.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}