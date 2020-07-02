package com.example.smartfoodcourt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import org.json.JSONObject;

import io.paperdb.Paper;

//public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
public class SignIn extends AppCompatActivity {

    TextView textSignUp, textForgotPassword;
    Button btnSignIn;
    EditText editPassword, editUserName;
    CheckBox checkBoxRemember;

    // Facebook
    private static final String TAG = SignIn.class.getSimpleName();
    CallbackManager callbackManager;
    LoginButton fbLoginButton;
    // Facebook

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //LoginFacebook
        loginFacebook();

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
        final DatabaseReference table_user = database.getReference("User");

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
                        Common.currentUser = user;
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
        final DatabaseReference table_user = database.getReference("User");

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
                        Common.currentUser = user;
                        startActivity(homePageIntent);
                        finish();
                    } else
                        Toast.makeText(SignIn.this, "Wrong Password !!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignIn.this, "User not exist in DataBase", Toast.LENGTH_LONG).show();
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

    // Login Facebook
    private void loginFacebook() {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton = (LoginButton) findViewById(R.id.btnLoginFacebook);

        fbLoginButton.setReadPermissions("email");

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "======Facebook login success======");
                Log.d(TAG, "Facebook Access Token: " + loginResult.getAccessToken().getToken());
                Toast.makeText(SignIn.this, "Login Facebook success.", Toast.LENGTH_SHORT).show();
                getFbInfo();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SignIn.this, "Login Facebook cancelled.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "======Facebook login error======");
                Log.e(TAG, "Error: " + error.toString());
                Toast.makeText(SignIn.this, "Login Facebook error.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFbInfo() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(final JSONObject me, GraphResponse response) {
                    if (me != null) {
                        Log.i("Login: ", me.optString("name"));
                        Log.i("ID: ", me.optString("id"));
                        Toast.makeText(SignIn.this, "Name: " + me.optString("name"), Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignIn.this, "ID: " + me.optString("id"), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();
        }
    }
}

