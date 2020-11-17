package com.example.fssupportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogInCenter extends AppCompatActivity {
    EditText email,password;
    ImageButton login;
    Button gotoRegis,gotoForgotPass;
    private ProgressDialog dialogNotificate;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_center);
        mAuth = FirebaseAuth.getInstance();
        dialogNotificate = new ProgressDialog(this);
        AnhXa();
        setEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void AnhXa(){
        email = (EditText) findViewById(R.id.txt_email_center);
        password = (EditText)findViewById(R.id.txt_password_center);
        login = (ImageButton)findViewById(R.id.btn_login_center);
        gotoRegis = (Button)findViewById(R.id.btn_goto_regis_center);
        gotoForgotPass= findViewById(R.id.btn_forgot_center);
    }
    private void Login() {
        dialogNotificate.show();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        if (Email.isEmpty() || Password.isEmpty()){
            Toast.makeText(LogInCenter.this, "Loiii!!!", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialogNotificate.dismiss();
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LogInCenter.this,HomeCenter.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(LogInCenter.this, "Loiii!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void setEvent(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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