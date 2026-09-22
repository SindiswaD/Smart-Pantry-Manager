package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Button btnViewAllRecipes;

    private ImageView navSettings;

    private EditText etSearchIngredients;

    private RecyclerView recyclerViewIngredients;

    private View cardEmptyPantry;
    private View cardNoSearchResults;

    private DatabaseHelper databaseHelper;
    private IngredientAdapter ingredientAdapter;

    /*
     * Contains the complete pantry.
     *
     * This is kept separate from the filtered
     * RecyclerView data so clearing the search
     * immediately restores all ingredients.
     */
    private List<Ingredient> ingredientList;

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

        btnViewAllRecipes =
                findViewById(
                        R.id.btnViewAllRecipes
                );

        navSettings =
                findViewById(
                        R.id.navSettings
                );

        etSearchIngredients =
                findViewById(
                        R.id.etSearchIngredients
                );

        recyclerViewIngredients =
                findViewById(
                        R.id.recyclerViewIngredients
                );

        cardEmptyPantry =
                findViewById(
                        R.id.cardEmptyPantry
                );

        cardNoSearchResults =
                findViewById(
                        R.id.cardNoSearchResults
                );

        /*
         * Create database helper.
         */
        databaseHelper =
                new DatabaseHelper(this);

        /*
         * Create the list used to keep the
         * complete pantry contents.
         */
        ingredientList =
                new ArrayList<>();

        /*
         * Create RecyclerView adapter.
         */
        ingredientAdapter =
                new IngredientAdapter(
                        new ArrayList<>(),
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
         *
         * This screen uses strict matching.
         * Only recipes where every required
         * ingredient is available in a sufficient
         * quantity are displayed.
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
         * VIEW ALL RECIPES
         *
         * This screen shows the complete recipe
         * collection together with pantry match
         * percentages and missing ingredients.
         */
        btnViewAllRecipes.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            AllRecipesActivity.class
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

        /*
         * SEARCH INGREDIENTS
         *
         * The RecyclerView updates immediately
         * as the user types.
         */
        etSearchIngredients.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {

                        // No action required.
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        filterIngredients(
                                s.toString()
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {

                        // No action required.
                    }
                }
        );
    }

    @Override
    protected void onResume() {

        super.onResume();

        /*
         * Reload the pantry whenever the user
         * returns to this screen.
         */
        loadIngredients();
    }

    private void loadIngredients() {

        List<Ingredient> savedIngredients =
                databaseHelper.getAllIngredients();

        /*
         * Keep expiry-priority sorting.
         */
        sortIngredientsByExpiry(
                savedIngredients
        );

        /*
         * Store the complete sorted pantry.
         */
        ingredientList =
                savedIngredients;

        /*
         * Preserve the current search when
         * returning from another Activity.
         */
        String currentSearch =
                etSearchIngredients
                        .getText()
                        .toString();

        if (savedIngredients.isEmpty()) {

            ingredientAdapter.updateData(
                    new ArrayList<>()
            );

            cardEmptyPantry.setVisibility(
                    View.VISIBLE
            );

            cardNoSearchResults.setVisibility(
                    View.GONE
            );

            recyclerViewIngredients.setVisibility(
                    View.GONE
            );

            etSearchIngredients.setEnabled(
                    false
            );

            /*
             * Strict recipe recommendations are
             * disabled when the pantry is empty
             * because nothing can be made.
             */
            btnFindRecipes.setEnabled(
                    false
            );

            btnFindRecipes.setAlpha(
                    0.5f
            );

            /*
             * All Recipes remains available.
             * Users can browse the recipe collection
             * even when their pantry is empty.
             */
            btnViewAllRecipes.setEnabled(
                    true
            );

            btnViewAllRecipes.setAlpha(
                    1.0f
            );

        } else {

            cardEmptyPantry.setVisibility(
                    View.GONE
            );

            etSearchIngredients.setEnabled(
                    true
            );

            btnFindRecipes.setEnabled(
                    true
            );

            btnFindRecipes.setAlpha(
                    1.0f
            );

            btnViewAllRecipes.setEnabled(
                    true
            );

            btnViewAllRecipes.setAlpha(
                    1.0f
            );

            /*
             * Display either the complete pantry
             * or the currently filtered result.
             */
            filterIngredients(
                    currentSearch
            );
        }
    }

    /*
     * Filter ingredients using the text entered
     * into the search field.
     */
    private void filterIngredients(
            String searchText
    ) {

        /*
         * If the actual pantry is empty,
         * the empty pantry state is handled
         * by loadIngredients().
         */
        if (ingredientList == null
                || ingredientList.isEmpty()) {

            return;
        }

        String searchQuery =
                searchText
                        .trim()
                        .toLowerCase();

        List<Ingredient> filteredIngredients =
                new ArrayList<>();

        /*
         * An empty search means show
         * the complete pantry.
         */
        if (searchQuery.isEmpty()) {

            filteredIngredients.addAll(
                    ingredientList
            );

        } else {

            for (Ingredient ingredient
                    : ingredientList) {

                String ingredientName =
                        ingredient
                                .getName()
                                .toLowerCase();

                /*
                 * Contains() allows partial searches.
                 *
                 * Examples:
                 *
                 * "mil" finds "Milk"
                 * "egg" finds "Eggs"
                 */
                if (ingredientName.contains(
                        searchQuery
                )) {

                    filteredIngredients.add(
                            ingredient
                    );
                }
            }
        }

        ingredientAdapter.updateData(
                filteredIngredients
        );

        /*
         * The pantry contains ingredients,
         * but none matched the search.
         */
        if (filteredIngredients.isEmpty()) {

            recyclerViewIngredients.setVisibility(
                    View.GONE
            );

            cardNoSearchResults.setVisibility(
                    View.VISIBLE
            );

        } else {

            recyclerViewIngredients.setVisibility(
                    View.VISIBLE
            );

            cardNoSearchResults.setVisibility(
                    View.GONE
            );
        }
    }

    /*
     * Sort ingredients according to expiry date.
     *
     * Earlier expiry dates appear first.
     * Ingredients without an expiry date appear
     * at the bottom.
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

                    if (date1 == null
                            && date2 == null) {

                        return 0;
                    }

                    if (date1 == null) {

                        return 1;
                    }

                    if (date2 == null) {

                        return -1;
                    }

                    return date1.compareTo(
                            date2
                    );
                }
        );
    }

    /*
     * Convert the stored expiry date into a
     * LocalDate so it can be sorted.
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