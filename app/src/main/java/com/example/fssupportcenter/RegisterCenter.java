package com.example.fssupportcenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fssupportcenter.Object.ObjectCenterSignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterCenter extends AppCompatActivity {

    private TextInputEditText center_name,center_email, center_phone,center_address, center_type,center_presenter;
    private Button center_regis;
    private String centername,centeremail, centerphone,centeraddress, centertype,centerpresenter;
    ObjectCenterSignUp centerSignUp;
    private ProgressDialog dialogNotificate;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_center);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        dialogNotificate = new ProgressDialog(this);
        AnhXa();
        setAction();
    }

    public void setAction(){
        center_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onRegisCenterAccount();
            }
        });
    }
    public void pushInformationAccount(){
        getValueOnField();
        mDatabase.child("AccountCenter").push().child("Email").setValue(centerSignUp);
    }
    public void getValueOnField(){
        centername = center_name.getText().toString();
        centeremail = center_email.getText().toString();
        centerphone = center_phone.getText().toString();
        centerSignUp = new ObjectCenterSignUp(centername,centeremail,centerphone);
    }

    public void onRegisCenterAccount(){
        CharSequence message = "Please waiting......";
        dialogNotificate.setMessage(message);
        dialogNotificate.show();
        getValueOnField();
        Calendar calendar = Calendar.getInstance();
        final String userName = centerSignUp.getCenterEmail();
        String passWord = ""+calendar.getTimeInMillis();
        mAuth.createUserWithEmailAndPassword(userName,passWord).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendLinkToChangePassword(userName);
                    pushInformationAccount();
                    dialogNotificate.dismiss();
                    Intent intent_gotoLogInCenter = new Intent(RegisterCenter.this, LogInCenter.class);
                    startActivity(intent_gotoLogInCenter);
                }else{
                    dialogNotificate.dismiss();
                    Toast.makeText(RegisterCenter.this, "This Email is already exists !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void sendLinkToChangePassword(String emailAdress){
        mAuth.sendPasswordResetEmail(emailAdress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(RegisterCenter.this, "Please check your mail, we already sent you a link to change your password!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(RegisterCenter.this, "Fail to send reset password email!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void AnhXa(){
        center_name = findViewById(R.id.edtxt_centerName);
        center_email = findViewById(R.id.edtxt_centerEmail);
        center_phone = findViewById(R.id.edtxt_centerPhone);
        center_regis = findViewById(R.id.btn_centerRegis);
    }
}