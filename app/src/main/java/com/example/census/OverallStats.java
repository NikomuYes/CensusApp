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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class OverallStats extends AppCompatActivity {

    private DatabaseReference userDataRef;
    private PieChart pieChart;
    private TextView userCountTextView;
    private TextView lastRegisteredUserTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        userDataRef = FirebaseDatabase.getInstance().getReference("userData");
        pieChart = findViewById(R.id.pieChart);
        userCountTextView = findViewById(R.id.userCountTextView);
        lastRegisteredUserTextView = findViewById(R.id.lastRegisteredUserTextView);

        loadChartData();
        updateUserCount();

        Button showLastRegisteredUserButton = findViewById(R.id.showLastRegisteredUserButton);
        showLastRegisteredUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLastRegisteredUserInformation();
            }
        });
    }

    private void loadChartData() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int maleCount = 0;
                int femaleCount = 0;
                int russianCount = 0;
                int ukranianCount = 0;
                int belarussianCount = 0;
                int kazakhstanianCount = 0;
                int inFamilyCount = 0;
                int outFamilyCount = 0;
                int middleOverallCount = 0;
                int middleProCount = 0;
                int highCount = 0;
                int otherCountryCount = 0;
                int unspecifiedCount = 0;
                int otherGenderCount = 0;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String gender = childSnapshot.child("gender").getValue(String.class);
                    String country = childSnapshot.child("country").getValue(String.class);
                    String family = childSnapshot.child("family").getValue(String.class);
                    String education = childSnapshot.child("education").getValue(String.class);

                    if (gender != null) {
                        if (gender.equals("Мужской")) {
                            maleCount++;
                        } else if (gender.equals("Женский")) {
                            femaleCount++;
                        } else {
                            otherGenderCount++;
                        }
                    }

                    if (country != null) {
                        if (country.equals("Россия")) {
                            russianCount++;
                        } else if (country.equals("Украина")) {
                            ukranianCount++;
                        } else if (country.equals("Беларусь")) {
                            belarussianCount++;
                        } else if (country.equals("Казахстан")) {
                            kazakhstanianCount++;
                        } else if (!country.isEmpty() && !country.equals("не указано")) {
                            otherCountryCount++;
                        } else {
                            unspecifiedCount++;
                        }
                    }

                    if (family != null) {
                        if (family.equals("В браке")) {
                            inFamilyCount++;
                        } else if (family.equals("Вне брака")) {
                            outFamilyCount++;
                        }
                    }

                    if (education != null) {
                        if (education.equals("Среднее общее")) {
                            middleOverallCount++;
                        } else if (education.equals("Среднее профессиональное")) {
                            middleProCount++;
                        } else if (education.equals("Высшее")) {
                            highCount++;
                        }
                    }
                }

                createPieChart(maleCount, femaleCount, otherGenderCount, russianCount, ukranianCount, belarussianCount, kazakhstanianCount, otherCountryCount, unspecifiedCount, inFamilyCount, outFamilyCount, middleOverallCount, middleProCount, highCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverallStats.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPieChart(int maleCount, int femaleCount, int otherGenderCount, int russianCount, int ukranianCount, int belarussianCount, int kazakhstanianCount, int otherCountryCount, int unspecifiedCount, int inFamilyCount, int outFamilyCount, int middleOverallCount, int middleProCount, int highCount) {
        ArrayList<PieEntry> genderEntries = new ArrayList<>();
        genderEntries.add(new PieEntry(maleCount, "Мужской"));
        genderEntries.add(new PieEntry(femaleCount, "Женский"));

        PieDataSet genderDataSet = new PieDataSet(genderEntries, "Соотношение полов");
        genderDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData genderData = new PieData(genderDataSet);

        pieChart.setData(genderData);
        pieChart.getDescription().setText("Соотношение полов");
        pieChart.invalidate(); // refresh

        PieChart nationalityChart = findViewById(R.id.nationalityPieChart);
        ArrayList<PieEntry> nationalityEntries = new ArrayList<>();
        nationalityEntries.add(new PieEntry(russianCount, "Россия"));
        nationalityEntries.add(new PieEntry(ukranianCount, "Украина"));
        nationalityEntries.add(new PieEntry(belarussianCount, "Беларусь"));
        nationalityEntries.add(new PieEntry(kazakhstanianCount, "Казахстан"));
        nationalityEntries.add(new PieEntry(otherCountryCount, "Другое"));

        PieDataSet nationalityDataSet = new PieDataSet(nationalityEntries, "Гражданство");
        nationalityDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData nationalityData = new PieData(nationalityDataSet);

        nationalityChart.setData(nationalityData);
        nationalityChart.getDescription().setText("Гражданство");
        nationalityChart.invalidate();

        PieChart familyChart = findViewById(R.id.familyPieChart);
        ArrayList<PieEntry> familyEntries = new ArrayList<>();
        familyEntries.add(new PieEntry(inFamilyCount, "В браке"));
        familyEntries.add(new PieEntry(outFamilyCount, "Вне брака"));

        PieDataSet familyDataSet = new PieDataSet(familyEntries, "Семейные положения");
        familyDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData familyData = new PieData(familyDataSet);

        familyChart.setData(familyData);
        familyChart.getDescription().setText("Семейные положения");
        familyChart.invalidate();

        PieChart educationChart = findViewById(R.id.educationPieChart);
        ArrayList<PieEntry> educationEntries = new ArrayList<>();
        educationEntries.add(new PieEntry(middleOverallCount, "Среднее общее"));
        educationEntries.add(new PieEntry(middleProCount, "Среднее профессиональное"));
        educationEntries.add(new PieEntry(highCount, "Высшее"));

        PieDataSet educationDataSet = new PieDataSet(educationEntries, "Образование");
        educationDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData educationData = new PieData(educationDataSet);

        educationChart.setData(educationData);
        educationChart.getDescription().setText("Образование");
        educationChart.invalidate(); // refresh
    }

    private void updateUserCount() {
        userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long userCount = dataSnapshot.getChildrenCount();
                userCountTextView.setText("Участников: " + userCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverallStats.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLastRegisteredUserInformation() {
        userDataRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String lastRegisteredUserName = childSnapshot.child("fullName").getValue(String.class);
                    String lastRegisteredUserAge = childSnapshot.child("age").getValue(String.class);
                    String lastRegisteredUserNation = childSnapshot.child("nation").getValue(String.class);
                    String lastRegisteredUserCountry = childSnapshot.child("country").getValue(String.class);
                    String lastRegisteredUserGender = childSnapshot.child("gender").getValue(String.class);
                    String lastRegisteredUserFamily = childSnapshot.child("family").getValue(String.class);
                    String lastRegisteredUserEducation = childSnapshot.child("education").getValue(String.class);

                    String userInfo = "Имя: " + lastRegisteredUserName + "\n" +
                            "Возраст: " + lastRegisteredUserAge + "\n" +
                            "Национальность: " + lastRegisteredUserNation + "\n" +
                            "Страна: " + lastRegisteredUserCountry + "\n" +
                            "Пол: " + lastRegisteredUserGender + "\n" +
                            "Семейное положение: " + lastRegisteredUserFamily + "\n" +
                            "Образование: " + lastRegisteredUserEducation;

                    lastRegisteredUserTextView.setText(userInfo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OverallStats.this, "Произошла ошибка при получении информации", Toast.LENGTH_SHORT).show();
            }
        });
    }
}