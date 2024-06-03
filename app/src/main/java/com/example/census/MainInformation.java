package com.example.census;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainInformation extends AppCompatActivity {

    public static final String KEY = "key";
    private String selectedCountry = "";
    private String selectedGender = "";

    private DatabaseReference userDataRef;
    private EditText fullNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maininformation);

        userDataRef = FirebaseDatabase.getInstance().getReference("userData");

        TextView textView = findViewById(R.id.textView2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phoneNumber = bundle.getString(KEY);
            textView.setText("Вы указали номер телефона: " + phoneNumber);
        }

        TextView countryTextView = findViewById(R.id.textView3);
        countryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelectionDialog();
            }
        });

        TextView genderTextView = findViewById(R.id.textView4);
        genderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderSelectionDialog();
            }
        });

        fullNameEditText = findViewById(R.id.editTextText);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullNameValid()) {
                    saveUserDataToDatabase();
                }
            }
        });
    }

    private boolean isFullNameValid() {
        String fullName = fullNameEditText.getText().toString().trim();
        if (fullName.isEmpty()) {
            Toast.makeText(this, "Поле \"Имя\" должно быть обязательно заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserDataToDatabase() {
        String phoneNumber = getIntent().getStringExtra(KEY);
        String fullName = fullNameEditText.getText().toString();

        userDataRef.child(phoneNumber).child("fullName").setValue(fullName);

        if (selectedCountry.isEmpty()) {
            selectedCountry = "не указано";
        }
        userDataRef.child(phoneNumber).child("country").setValue(selectedCountry);

        if (selectedGender.isEmpty()) {
            selectedGender = "не указано";
        }
        userDataRef.child(phoneNumber).child("gender").setValue(selectedGender);

        Toast.makeText(this, "Информация сохранена в базе данных", Toast.LENGTH_SHORT).show();
    }

    private void showCountrySelectionDialog() {
        final CharSequence[] options = {"Российское", "Другое"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите ваше гражданство");
        builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedCountry = options[which].toString();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainInformation.this, "Выбрано: " + selectedCountry, Toast.LENGTH_SHORT).show();
                TextView selectedCountryTextView = findViewById(R.id.selectedCountryTextView);
                selectedCountryTextView.setText("Ваше гражданство: " + selectedCountry);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showGenderSelectionDialog() {
        final CharSequence[] options = {"Мужской", "Женский"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите ваш пол");
        builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedGender = options[which].toString();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainInformation.this, "Выбрано: " + selectedGender, Toast.LENGTH_SHORT).show();
                TextView selectedGenderTextView = findViewById(R.id.selectedGenderTextView);
                selectedGenderTextView.setText("Ваш пол: " + selectedGender);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}