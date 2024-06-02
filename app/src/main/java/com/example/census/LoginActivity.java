package com.example.census;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText editLogin, editPassword;
    private FirebaseAuth myAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(this, "Есть регистрация", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Нет регистрации", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        editLogin = findViewById(R.id.editTextLogin);
        editPassword = findViewById(R.id.editTextPassword);
        myAuth = FirebaseAuth.getInstance();
    }
    public void onClickSignUp(View view) {
        if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {
            myAuth.createUserWithEmailAndPassword(editLogin.getText().toString(), editPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // ?
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Что-то пошло не так, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Есть незаполненные поля", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickSignIn(View view) {
        if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {
            myAuth.signInWithEmailAndPassword(editLogin.getText().toString(), editPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { // ?
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Вы вошли!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Что-то пошло не так, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
