package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvBack;
    private TextView tvRecipeName;
    private TextView tvIngredients;
    private TextView tvRecipeInstructions;

    private DatabaseHelper databaseHelper;

    private int recipeId;
    private String recipeName;
    private String recipeInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_recipe_detail
        );

        // Connect Java variables to the views in the XML layout.
        tvBack =
                findViewById(R.id.tvBack);

        tvRecipeName =
                findViewById(R.id.tvRecipeName);

        tvIngredients =
                findViewById(R.id.tvIngredients);

        tvRecipeInstructions =
                findViewById(
                        R.id.tvRecipeInstructions
                );

        databaseHelper =
                new DatabaseHelper(this);

        /*
         * Receive the selected recipe information
         * from RecipeRecommendationsActivity.
         */
        recipeId =
                getIntent().getIntExtra(
                        "recipe_id",
                        -1
                );

        recipeName =
                getIntent().getStringExtra(
                        "recipe_name"
                );

        recipeInstructions =
                getIntent().getStringExtra(
                        "recipe_instructions"
                );

        /*
         * Display the recipe name.
         */
        if (recipeName != null) {

            tvRecipeName.setText(
                    recipeName
            );
        }

        /*
         * Display the recipe method.
         */
        if (recipeInstructions != null) {

            tvRecipeInstructions.setText(
                    recipeInstructions
            );
        }

        /*
         * Load the ingredients belonging
         * to this recipe from SQLite.
         */
        loadRecipeIngredients();

        /*
         * Return to the recipe recommendations screen.
         */
        tvBack.setOnClickListener(v ->
                getOnBackPressedDispatcher()
                        .onBackPressed()
        );
    }

    private void loadRecipeIngredients() {

        /*
         * A valid recipe ID must have been
         * received from the previous screen.
         */
        if (recipeId == -1) {

            tvIngredients.setText(
                    "Recipe ingredients unavailable."
            );

            return;
        }

        /*
         * Get all ingredient requirements
         * belonging to the selected recipe.
         */
        List<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(
                        recipeId
                );

        /*
         * Handle the unlikely case where
         * the recipe has no ingredient records.
         */
        if (ingredients == null ||
                ingredients.isEmpty()) {

            tvIngredients.setText(
                    "No ingredients found."
            );

            return;
        }

        /*
         * Build one readable ingredient list
         * for the TextView.
         */
        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(
                            formatQuantity(
                                    ingredient.getRequiredQuantity()
                            )
                    )
                    .append(" ")
                    .append(
                            ingredient.getUnit()
                    )
                    .append(" ")
                    .append(
                            ingredient.getIngredientName()
                    )
                    .append("\n");
        }

        /*
         * Remove the final unnecessary
         * line break.
         */
        if (ingredientText.length() > 0) {

            ingredientText.setLength(
                    ingredientText.length() - 1
            );
        }

        tvIngredients.setText(
                ingredientText.toString()
        );
    }

    /*
     * Prevent quantities such as 2.0
     * from being displayed when 2 is cleaner.
     *
     * Decimal quantities such as 1.5
     * are kept as decimals.
     */
    private String formatQuantity(
            double quantity
    ) {

        if (quantity ==
                Math.floor(quantity)) {

            return String.valueOf(
                    (int) quantity
            );
        }

        return String.valueOf(
                quantity
        );
    }
}