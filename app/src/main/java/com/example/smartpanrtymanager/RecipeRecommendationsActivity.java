package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecommendationsActivity
        extends AppCompatActivity {

    private TextView tvBack;
    private RecyclerView recyclerViewRecipes;
    private CardView cardNoRecipes;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    private List<Recipe> recommendedRecipes;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_recommendations
        );

        tvBack =
                findViewById(R.id.tvBack);

        recyclerViewRecipes =
                findViewById(
                        R.id.recyclerViewRecipes
                );

        cardNoRecipes =
                findViewById(
                        R.id.cardNoRecipes
                );

        databaseHelper =
                new DatabaseHelper(this);

        recommendedRecipes =
                new ArrayList<>();

        /*
         * When a recipe is clicked,
         * open the Recipe Detail screen.
         */
        recipeAdapter =
                new RecipeAdapter(
                        recommendedRecipes,
                        recipe -> openRecipeDetails(recipe)
                );

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewRecipes.setAdapter(
                recipeAdapter
        );

        /*
         * Return to the pantry screen.
         */
        tvBack.setOnClickListener(v ->
                getOnBackPressedDispatcher()
                        .onBackPressed()
        );

        loadRecipeRecommendations();
    }

    @Override
    protected void onResume() {

        super.onResume();

        loadRecipeRecommendations();
    }

    private void loadRecipeRecommendations() {

        List<Recipe> recipes =
                databaseHelper
                        .getRecommendedRecipes();

        recipeAdapter.updateData(recipes);

        if (recipes.isEmpty()) {

            recyclerViewRecipes.setVisibility(
                    View.GONE
            );

            cardNoRecipes.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerViewRecipes.setVisibility(
                    View.VISIBLE
            );

            cardNoRecipes.setVisibility(
                    View.GONE
            );
        }
    }

    private void openRecipeDetails(
            Recipe recipe
    ) {

        Intent intent =
                new Intent(
                        RecipeRecommendationsActivity.this,
                        RecipeDetailActivity.class
                );

        /*
         * Send the selected recipe information
         * to the detail screen.
         */
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