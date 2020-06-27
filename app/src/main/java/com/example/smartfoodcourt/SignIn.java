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

import org.json.JSONObject;

//public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
public class SignIn extends AppCompatActivity {

    TextView textSignUp, textForgotPassword;
    Button btnSignIn;
    EditText editPassword, editUserName;

    // Facebook
    private static final String TAG = SignIn.class.getSimpleName();
    CallbackManager callbackManager;
    LoginButton fbLoginButton;
    // Facebook

//    //Google
//    private GoogleApiClient mGoogleApiClient;
//    private TextView mStatusTextView;
//    private ProgressDialog mProgressDialog;
//
//    private static final String TAGGoogle = "MainActivity";
//    private static final int RC_SIGN_IN = 9001;
//    //Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        //Login Google
//
//        // Button listeners
//        findViewById(R.id.btnSignInGoogle).setOnClickListener(this);
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
//
//        SignInButton signInButton = (SignInButton) findViewById(R.id.btnSignInGoogle);
//        signInButton.setSize(SignInButton.SIZE_STANDARD);
//        signInButton.setScopes(gso.getScopeArray());


        // Login Facebook
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


        // Text SignUp
        textSignUp = (TextView)findViewById(R.id.text_sign_up);
        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        // Sign In
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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                ///Toast.makeText( SignIn.this, "OK OK OK", Toast.LENGTH_SHORT).show();
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
        });
    }

    // Facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFbInfo() {
        if (AccessToken.getCurrentAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
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

    //Google
//    @Override
//    protected void onStart() {
//        super.onStart();
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // Nếu dữ liệu của người dùng trong bộ d dệm hợp lệ, OptionalPendingResult sẽ ở trạng thái "done"
//            // và GoogleSignInResult sẽ có ngay mà không cần thực hiện đăng nhập lại.
//            Log.d(TAGGoogle, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // Nếu người dùng chưa từng đăng nhập trước đó, hoặc phiên làm việc đã hết hạn,
//            // thao tác bất đồng bộ này sẽ ngầm đăng nhập người dùng, và thực hiện thao tác cross sign-on.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btnSignInGoogle:
//                signIn();
//                break;
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.e(TAGGoogle, "onConnectionFailed:" + connectionResult);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//            handleSignInResult(result);
//        }
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAGGoogle, "handleSignInResult:" + result.isSuccess());
//        if (result.isSuccess()) {
//            // Đã đăng nhập thành công, hiển thị trạng thái đăng nhập.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            mStatusTextView.setText(acct.getDisplayName());
//        }
//    }
//
//    private void signIn() {
//        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
////    private void signOut() {
////        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
////                new ResultCallback<Status>() {
////                    @Override
////                    public void onResult(Status status) {
////                        mStatusTextView.setText("Signed out");
////                        Toast.makeText(SignIn.this, "Signed out", Toast.LENGTH_SHORT).show();
////                    }
////                });
////    }
//
//    private void showProgressDialog() {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setMessage("Loading");
//            mProgressDialog.setIndeterminate(true);
//        }
//
//        mProgressDialog.show();
//    }
//
//    private void hideProgressDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.hide();
//        }
//    }
}

