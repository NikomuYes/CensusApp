package com.example.census;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public static final String KEY = "key";
    private DatabaseReference userDataRef;
    private DatabaseReference phoneNumberRef; // новое поле для ссылки на узел с номерами телефонов
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userDataRef = database.getReference("userData");
        phoneNumberRef = database.getReference("phoneNumbers"); // Инициализация ссылки на узел с номерами телефонов

        phoneEditText = findViewById(R.id.editTextPhone);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::onClick);
    }

    private void onClick(View v) {
        if (v.getId() == R.id.button) {
            String phoneNumber = phoneEditText.getText().toString().trim();

            if (phoneNumber.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Пожалуйста, укажите свой номер телефона", Toast.LENGTH_SHORT).show();
            } else {
                if (phoneNumber.equals("987654321")) {
                    showPasswordDialog();
                } else {
                    checkIfPhoneNumberExists(phoneNumber);
                }
            }
        }
    }


    private void checkIfPhoneNumberExists(final String phoneNumber) {
        phoneNumberRef.child(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Номер телефона уже существует в базе данных
                    Toast.makeText(getApplicationContext(), "Этот пользователь уже принял участие в переписи населения", Toast.LENGTH_SHORT).show();
                } else {
                    // Номер телефона уникален, сохраняем его в базу данных и переходим к следующему действию
                    phoneNumberRef.child(phoneNumber).setValue(true); // Сохранение номера телефона в узле с номерами телефонов
                    startSecondActivity(phoneNumber);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Обработка ошибок, если операция была отменена
            }
        });
    }

    private void startSecondActivity(String phoneNumber) {
        Intent intent = new Intent(MainActivity.this, MainInformation.class);
        intent.putExtra(KEY, phoneNumber);
        startActivity(intent);
    }


    private void showPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Введите пароль");

        // Добавляем поле для ввода пароля в диалоговое окно
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();

                // Проверяем пароль
                if (password.equals("g-301-v")) {
                    String phoneNumber = phoneEditText.getText().toString().trim();  // Получаем номер телефона из EditText
                    Intent intent = new Intent(MainActivity.this, OverallStats.class);
                    intent.putExtra(KEY, phoneNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Неверный пароль", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}