package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private TextView navPantry;
    private TextView navSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        navPantry = findViewById(R.id.navPantry);
        navSettings = findViewById(R.id.navSettings);

        // Go back to the Pantry screen
        navPantry.setOnClickListener(v -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    MainActivity.class
            );

            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP
            );

            startActivity(intent);
            finish();
        });

        // We are already on the Settings screen
        navSettings.setOnClickListener(v -> {
            // No action needed
        });
    }
}