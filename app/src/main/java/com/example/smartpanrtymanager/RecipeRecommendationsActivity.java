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

public class RecipeRecommendationsActivity extends AppCompatActivity {

    private TextView tvBack;
    private RecyclerView recyclerViewRecipes;
    private CardView cardNoRecipes;

    private DatabaseHelper databaseHelper;
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_recommendations
        );

        /*
         * Connect views.
         */
        tvBack =
                findViewById(
                        R.id.tvBack
                );

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

        /*
         * Start the adapter with an empty list.
         * Strict recipe recommendations are loaded
         * from the database below.
         */
        recipeAdapter =
                new RecipeAdapter(
                        new ArrayList<>(),
                        this::openRecipeDetails
                );

        recyclerViewRecipes.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerViewRecipes.setAdapter(
                recipeAdapter
        );

        /*
         * Return to the previous screen.
         *
         * This uses the same approach as
         * AllRecipesActivity.
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

        /*
         * Reload recommendations whenever the
         * user returns to this screen.
         */
        loadRecipeRecommendations();
    }

    private void loadRecipeRecommendations() {

        /*
         * getRecommendedRecipes() applies the
         * strict pantry matching rules.
         */
        List<Recipe> recipes =
                databaseHelper
                        .getRecommendedRecipes();

        recipeAdapter.updateData(
                recipes
        );

        /*
         * Show feedback when there are no
         * complete recipe matches.
         */
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