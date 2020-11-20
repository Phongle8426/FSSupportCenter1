package com.example.fssupportcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogInCenter extends AppCompatActivity {
    TextInputEditText email,password;
    Button login;
    TextView gotoRegis,gotoForgotPass;
    CheckBox rememberMe;
    ProgressBar progressBar;
    public static final String MyPREFERENCES = "referLogin"; //biến lưu của cặp user/password
    public static final String USERNAME = "centerNameKey"; // biến lưu username
    public static final String PASS = "passKey";        // biến lưu password
    public static final String REMEMBER = "remember";  // biến lưu lựa chọn remember me
    SharedPreferences sharedpreferences; // khai báo sharedpreference để lưu cặp user/passs
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        AnhXa();
        loadData();
        setEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void AnhXa(){
        email = findViewById(R.id.txt_email_center);
        password = findViewById(R.id.txt_password_center);
        login = findViewById(R.id.btn_login);
        gotoRegis = findViewById(R.id.btn_goto_regis_center);
        gotoForgotPass= findViewById(R.id.btn_forgot_center);
        progressBar = findViewById(R.id.progress);
        rememberMe = findViewById(R.id.cb_remember);
        progressBar.setVisibility(View.INVISIBLE);
    }
    private void Login() {
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        if (Email.isEmpty() || Password.isEmpty()){
            Toast.makeText(this, "Email or Password is required.", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }else{
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (rememberMe.isChecked())
                                    saveData(Email,Password);
                                else
                                    clearData();
                                Intent intent_goToHome = new Intent(LogInCenter.this,HomeCenter.class);
                                startActivity(intent_goToHome);
                            }else {
                                Toast.makeText(LogInCenter.this, "Login fail.Check your info again !", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }


    public void saveData(String username, String Pass){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,rememberMe.isChecked());
        editor.commit();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            email.setText(sharedpreferences.getString(USERNAME, ""));
            password.setText(sharedpreferences.getString(PASS, ""));
            rememberMe.setChecked(true);
            Login();
        }
        else
            rememberMe.setChecked(false);

    }
    public void setEvent(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Login();

            }
        });
        gotoRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInCenter.this,RegisterCenter.class);
                startActivity(intent);
            }
        });

        gotoForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_toForgotPass = new Intent(LogInCenter.this,ForgotPassword.class);
                startActivity(intent_toForgotPass);
            }
        });
    }
}