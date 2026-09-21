package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    /*
     * The format currently used when ingredient
     * expiry dates are stored in the database.
     *
     * Examples:
     * 2/9/2026
     * 21/9/2026
     * 26/9/2026
     */
    private final DateTimeFormatter expiryDateFormatter =
            DateTimeFormatter.ofPattern(
                    "d/M/yyyy"
            );

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_main
        );

        /*
         * Connect Java variables to XML views.
         */
        btnAddIngredient =
                findViewById(
                        R.id.btnAddIngredient
                );

        btnFindRecipes =
                findViewById(
                        R.id.btnFindRecipes
                );

        navSettings =
                findViewById(
                        R.id.navSettings
                );

        recyclerViewIngredients =
                findViewById(
                        R.id.recyclerViewIngredients
                );

        cardEmptyPantry =
                findViewById(
                        R.id.cardEmptyPantry
                );

        /*
         * Create the database helper.
         */
        databaseHelper =
                new DatabaseHelper(this);

        /*
         * Create the ingredient list used
         * by the RecyclerView.
         */
        ingredientList =
                new ArrayList<>();

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

        /*
         * ADD INGREDIENT
         */
        btnAddIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AddEditIngredientActivity.class
                    );

            startActivity(intent);
        });

        /*
         * WHAT CAN I MAKE?
         */
        btnFindRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            RecipeRecommendationsActivity.class
                    );

            startActivity(intent);
        });

        /*
         * SETTINGS
         */
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

        /*
         * Reload the pantry whenever the user
         * returns to this screen.
         *
         * This ensures newly added, edited or
         * deleted ingredients appear immediately.
         */
        loadIngredients();
    }

    private void loadIngredients() {

        List<Ingredient> savedIngredients =
                databaseHelper.getAllIngredients();

        /*
         * Sort ingredients by expiry urgency
         * before displaying them.
         *
         * Expired ingredients appear first,
         * followed by the nearest upcoming
         * expiry dates.
         *
         * Ingredients without an expiry date
         * appear at the bottom.
         */
        sortIngredientsByExpiry(
                savedIngredients
        );

        ingredientAdapter.updateData(
                savedIngredients
        );

        /*
         * Display the empty pantry card when
         * there are no saved ingredients.
         */
        if (savedIngredients.isEmpty()) {

            cardEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            recyclerViewIngredients.setVisibility(
                    View.GONE
            );

            btnFindRecipes.setEnabled(
                    false
            );

            btnFindRecipes.setAlpha(
                    0.5f
            );

        } else {

            cardEmptyPantry.setVisibility(
                    View.GONE
            );

            recyclerViewIngredients.setVisibility(
                    View.VISIBLE
            );

            btnFindRecipes.setEnabled(
                    true
            );

            btnFindRecipes.setAlpha(
                    1.0f
            );
        }
    }

    /*
     * Sort the pantry according to expiry date.
     *
     * Example:
     *
     * Expired yesterday
     * Expires today
     * Expires tomorrow
     * Expires in 5 days
     * Expires next month
     * No expiry date
     */
    private void sortIngredientsByExpiry(
            List<Ingredient> ingredients
    ) {

        ingredients.sort(
                (ingredient1, ingredient2) -> {

                    LocalDate date1 =
                            parseExpiryDate(
                                    ingredient1
                                            .getExpiryDate()
                            );

                    LocalDate date2 =
                            parseExpiryDate(
                                    ingredient2
                                            .getExpiryDate()
                            );

                    /*
                     * Neither ingredient has a
                     * usable expiry date.
                     *
                     * Keep their existing order.
                     */
                    if (date1 == null
                            && date2 == null) {

                        return 0;
                    }

                    /*
                     * Ingredient 1 has no expiry
                     * date, so place it after
                     * ingredient 2.
                     */
                    if (date1 == null) {

                        return 1;
                    }

                    /*
                     * Ingredient 2 has no expiry
                     * date, so place it after
                     * ingredient 1.
                     */
                    if (date2 == null) {

                        return -1;
                    }

                    /*
                     * Earlier dates have greater
                     * urgency and therefore appear
                     * first.
                     */
                    return date1.compareTo(
                            date2
                    );
                }
        );
    }

    /*
     * Convert the expiry date String stored in
     * SQLite into a LocalDate that can be sorted.
     *
     * Returning null allows ingredients with no
     * expiry date, or an unexpected date format,
     * to safely move to the bottom of the list.
     */
    private LocalDate parseExpiryDate(
            String expiryDate
    ) {

        if (expiryDate == null
                || expiryDate.trim().isEmpty()) {

            return null;
        }

        try {

            return LocalDate.parse(
                    expiryDate.trim(),
                    expiryDateFormatter
            );

        } catch (DateTimeParseException e) {

            return null;
        }
    }
}