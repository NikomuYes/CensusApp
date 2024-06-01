package com.example.census;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private EditText editLog, editPas;
    private DatabaseReference loginDatabase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
    }
    private void init(){
        editLog = findViewById(R.id.editLogin);
        editPas = findViewById(R.id.editPassword);
        loginDatabase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }
    public void onClickLogin(View view) {
        String id = loginDatabase.getKey();
        String login = editLog.getText().toString();
        String password = editPas.getText().toString();
        User newUser = new User(id, login, password);
        if (!TextUtils.isEmpty(login) && !TextUtils.isEmpty(password)) {
            loginDatabase.push().setValue(newUser);
        }
        else {
            Toast.makeText(this, "Вы не заполнили какое-то поле", Toast.LENGTH_SHORT).show();
        }
    }
}