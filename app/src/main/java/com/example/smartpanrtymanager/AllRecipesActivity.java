package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllRecipesActivity extends AppCompatActivity {

    private TextView tvBack;
    private RecyclerView recyclerViewAllRecipes;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_all_recipes
        );

        tvBack =
                findViewById(
                        R.id.tvBack
                );

        recyclerViewAllRecipes =
                findViewById(
                        R.id.recyclerViewAllRecipes
                );

        databaseHelper =
                new DatabaseHelper(this);

        /*
         * Start the adapter with an empty list.
         * The actual recipe information is loaded
         * from the database below.
         */
        recipeAdapter =
                new RecipeAdapter(
                        new ArrayList<>(),
                        this::openRecipeDetails
                );

        recyclerViewAllRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewAllRecipes.setAdapter(
                recipeAdapter
        );

        /*
         * Return to the previous screen.
         */
        tvBack.setOnClickListener(v ->
                getOnBackPressedDispatcher()
                        .onBackPressed()
        );

        loadAllRecipes();
    }

    @Override
    protected void onResume() {

        super.onResume();

        /*
         * Reload match percentages whenever
         * the user returns to this screen.
         *
         * This keeps the percentages accurate
         * if pantry contents have changed.
         */
        loadAllRecipes();
    }

    private void loadAllRecipes() {

        /*
         * Unlike Suggested Recipes, this method
         * does NOT remove incomplete recipes.
         *
         * Every stored recipe is returned with
         * its pantry match information.
         */
        List<Recipe> recipes =
                databaseHelper
                        .getAllRecipesWithMatchInformation();

        recipeAdapter.updateData(
                recipes
        );
    }

    private void openRecipeDetails(
            Recipe recipe
    ) {

        Intent intent =
                new Intent(
                        AllRecipesActivity.this,
                        RecipeDetailActivity.class
                );

        intent.putExtra(
                "recipe_id",
                recipe.getId()
        );

        intent.putExtra(
                "recipe_name",
                recipe.getName()
        );

        intent.putExtra(
                "recipe_instructions",
                recipe.getInstructions()
        );

        startActivity(intent);
    }
}