package com.example.census;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OverallStats extends AppCompatActivity {

    private DatabaseReference userDataRef;
    private TextView userCountTextView;
    private TextView lastRegisteredUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        userDataRef = FirebaseDatabase.getInstance().getReference("userData");
        userCountTextView = findViewById(R.id.userCountTextView);
        lastRegisteredUserTextView = findViewById(R.id.lastRegisteredUserTextView);

        Button showLastRegisteredUserButton = findViewById(R.id.showLastRegisteredUserButton);
        showLastRegisteredUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastRegisteredUserInformation();
            }
        });

        updateUserCount();
    }

    private void showLastRegisteredUserInformation() {
        userDataRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String lastRegisteredUserName = childSnapshot.child("fullName").getValue(String.class);
                    String lastRegisteredUserCountry = childSnapshot.child("country").getValue(String.class);
                    String lastRegisteredUserGender = childSnapshot.child("gender").getValue(String.class);

                    String userInfo = "Имя: " + lastRegisteredUserName + "\n" +
                            "Страна: " + lastRegisteredUserCountry + "\n" +
                            "Пол: " + lastRegisteredUserGender;

                    lastRegisteredUserTextView.setText(userInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverallStats.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserCount() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long userCount = dataSnapshot.getChildrenCount();
                userCountTextView.setText("Количество зарегистрированных пользователей: " + userCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverallStats.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }
}