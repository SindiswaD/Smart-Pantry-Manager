package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnAddIngredient;

    private RecyclerView recyclerViewIngredients;

    private View cardEmptyPantry;

    private DatabaseHelper databaseHelper;

    private IngredientAdapter ingredientAdapter;

    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        recyclerViewIngredients =
                findViewById(R.id.recyclerViewIngredients);

        cardEmptyPantry =
                findViewById(R.id.cardEmptyPantry);

        databaseHelper =
                new DatabaseHelper(this);

        ingredientList =
                new ArrayList<>();

        /*
         * loadIngredients is passed to the adapter.
         * This allows the main screen to refresh
         * immediately after an ingredient is deleted.
         */
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

        btnAddIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    AddEditIngredientActivity.class
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

        } else {

            cardEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerViewIngredients.setVisibility(
                    View.VISIBLE
            );
        }
    }
}