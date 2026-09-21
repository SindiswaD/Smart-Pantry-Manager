package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAddIngredient;
    private Button btnFindRecipes;

    private ImageView navSettings;

    private RecyclerView recyclerViewIngredients;
    private View cardEmptyPantry;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Connect Java variables to XML views
        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        btnFindRecipes =
                findViewById(R.id.btnFindRecipes);

        navSettings =
                findViewById(R.id.navSettings);

        recyclerViewIngredients =
                findViewById(R.id.recyclerViewIngredients);

        cardEmptyPantry =
                findViewById(R.id.cardEmptyPantry);

        // Create database helper
        databaseHelper =
                new DatabaseHelper(this);

        // Create empty ingredient list
        ingredientList =
                new ArrayList<>();

        // Create RecyclerView adapter
        ingredientAdapter =
                new IngredientAdapter(
                        ingredientList,
                        this::loadIngredients
                );

        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewIngredients.setAdapter(
                ingredientAdapter
        );

        // ADD INGREDIENT BUTTON
        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class
                    );

            startActivity(intent);
        });

        // WHAT CAN I MAKE BUTTON
        btnFindRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            RecipeRecommendationsActivity.class
                    );

            startActivity(intent);
        });

        // SETTINGS BUTTON
        navSettings.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            SettingsActivity.class
                    );

            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadIngredients();
    }

    private void loadIngredients() {

        List<Ingredient> savedIngredients =
                databaseHelper.getAllIngredients();

        ingredientAdapter.updateData(
                savedIngredients
        );

        if (savedIngredients.isEmpty()) {

            cardEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            recyclerViewIngredients.setVisibility(
                    View.GONE
            );

            btnFindRecipes.setEnabled(false);
            btnFindRecipes.setAlpha(0.5f);

        } else {

            cardEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerViewIngredients.setVisibility(
                    View.VISIBLE
            );

            btnFindRecipes.setEnabled(true);
            btnFindRecipes.setAlpha(1.0f);
        }
    }
}