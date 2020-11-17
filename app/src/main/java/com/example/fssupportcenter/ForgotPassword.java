package com.example.fssupportcenter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class ForgotPassword extends AppCompatActivity {


    EditText email_forgot;
    Button forgot;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mAuth = FirebaseAuth.getInstance();
        AnhXa();
        setEvent(); // function request link to change password
    }

    public void AnhXa(){
        email_forgot = (EditText)findViewById(R.id.txt_email_forgot);
        forgot = (Button) findViewById(R.id.btn_forgot);
    }

    // click Next button to request
    public void setEvent(){
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_forgot.getText().toString();
                sendLinkToChangePassword(email);
            }
        });
    }

    // send the link reset password to the email user enter.
    public void sendLinkToChangePassword(String emailAdress){
        mAuth.sendPasswordResetEmail(emailAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                Toast.makeText(ForgotPassword.this, "Please checked your email to reset your password!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ForgotPassword.this, "Fail to send reset password email!", Toast.LENGTH_LONG).show();
            }
        });
    }
}