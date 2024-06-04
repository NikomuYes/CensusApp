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

    private String selectedFamily = "";
    private String selectedEducation = "";

    private DatabaseReference userDataRef;
    private EditText fullNameEditText;
    private EditText ageEditText;
    private EditText nationEditText;

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

        TextView familyTextView = findViewById(R.id.textView5);
        familyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFamilySelectionDialog();
            }
        });

        TextView educationTextView = findViewById(R.id.textView6);
        educationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEducationSelectionDialog();
            }
        });

        fullNameEditText = findViewById(R.id.editTextText);
        ageEditText = findViewById(R.id.editTextAge);
        nationEditText = findViewById(R.id.editTextNation);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullNameValid() && isAgeValid() && isNationValid()){
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

    private boolean isAgeValid() {
        String age = ageEditText.getText().toString().trim();
        if (age.isEmpty()) {
            Toast.makeText(this, "Поле \"Возраст\" должно быть обязательно заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isNationValid() {
        String nation = nationEditText.getText().toString().trim();
        if (nation.isEmpty()) {
            Toast.makeText(this, "Поле \"Национальность\" должно быть обязательно заполнено", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void saveUserDataToDatabase() {
        String phoneNumber = getIntent().getStringExtra(KEY);
        String fullName = fullNameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String nation = nationEditText.getText().toString();

        userDataRef.child(phoneNumber).child("fullName").setValue(fullName);
        userDataRef.child(phoneNumber).child("age").setValue(age);
        userDataRef.child(phoneNumber).child("nation").setValue(nation);

        if (selectedCountry.isEmpty()) {
            selectedCountry = "Не указано";
        }
        userDataRef.child(phoneNumber).child("country").setValue(selectedCountry);

        if (selectedGender.isEmpty()) {
            selectedGender = "Не указано";
        }
        userDataRef.child(phoneNumber).child("gender").setValue(selectedGender);

        if (selectedFamily.isEmpty()) {
            selectedFamily = "Не указано";
        }
        userDataRef.child(phoneNumber).child("family").setValue(selectedFamily);

        if (selectedEducation.isEmpty()) {
            selectedEducation = "Не указано";
        }
        userDataRef.child(phoneNumber).child("education").setValue(selectedEducation);

        Toast.makeText(this, "Информация сохранена в базе данных", Toast.LENGTH_SHORT).show();
    }

    private void showCountrySelectionDialog() {
        final CharSequence[] options = {"Россия", "Украина", "Беларусь", "Казахстан", "Другое"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите вашу страну");
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
                selectedCountryTextView.setText(selectedCountry);
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
                selectedGenderTextView.setText(selectedGender);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showFamilySelectionDialog() {
        final CharSequence[] options = {"В браке", "Вне брака"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите ваше семейное положение");
        builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedFamily = options[which].toString();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainInformation.this, "Выбрано: " + selectedFamily, Toast.LENGTH_SHORT).show();
                TextView selectedGenderTextView = findViewById(R.id.selectedFamilyTextView);
                selectedGenderTextView.setText(selectedFamily);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showEducationSelectionDialog() {
        final CharSequence[] options = {"Среднее общее", "Среднее профессиональное", "Высшее"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите ваше образование");
        builder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedEducation = options[which].toString();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainInformation.this, "Выбрано: " + selectedEducation, Toast.LENGTH_SHORT).show();
                TextView selectedGenderTextView = findViewById(R.id.selectedEducationTextView);
                selectedGenderTextView.setText(selectedEducation);
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}