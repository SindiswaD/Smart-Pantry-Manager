package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;

    private Spinner spinnerUnit;

    private Button btnSaveIngredient;
    private Button btnCancel;

    private TextView tvBack;
    private TextView tvFormTitle;

    private DatabaseHelper databaseHelper;

    private int ingredientId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_ingredient);

        // Connect Java variables to XML views
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);

        spinnerUnit = findViewById(R.id.spinnerUnit);

        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        btnCancel = findViewById(R.id.btnCancel);

        tvBack = findViewById(R.id.tvBack);
        tvFormTitle = findViewById(R.id.tvFormTitle);

        // Create database helper
        databaseHelper = new DatabaseHelper(this);

        // Set up form features
        setupUnitSpinner();
        setupDatePicker();

        // Check if this screen was opened in edit mode
        checkForEditMode();

        // Back button
        tvBack.setOnClickListener(v -> finish());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());

        // Save / Update button
        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void setupUnitSpinner() {

        String[] units = {
                "Select unit",
                "Items",
                "g",
                "kg",
                "ml",
                "L",
                "cups",
                "tbsp",
                "tsp"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);
    }

    private void setupDatePicker() {

        etExpiryDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(
                            AddEditIngredientActivity.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                String selectedDate =
                                        selectedDay + "/" +
                                                (selectedMonth + 1) + "/" +
                                                selectedYear;

                                etExpiryDate.setText(selectedDate);
                            },
                            year,
                            month,
                            day
                    );

            datePickerDialog.show();
        });
    }

    private void checkForEditMode() {

        if (getIntent().hasExtra("ingredient_id")) {

            isEditMode = true;

            ingredientId =
                    getIntent().getIntExtra(
                            "ingredient_id",
                            -1
                    );

            String ingredientName =
                    getIntent().getStringExtra(
                            "ingredient_name"
                    );

            double ingredientQuantity =
                    getIntent().getDoubleExtra(
                            "ingredient_quantity",
                            0
                    );

            String ingredientUnit =
                    getIntent().getStringExtra(
                            "ingredient_unit"
                    );

            String ingredientExpiry =
                    getIntent().getStringExtra(
                            "ingredient_expiry"
                    );

            // Change screen text for edit mode
            tvFormTitle.setText(
                    "Edit Ingredient"
            );

            btnSaveIngredient.setText(
                    "Update Ingredient"
            );

            // Fill existing values into the form
            etIngredientName.setText(
                    ingredientName
            );

            etQuantity.setText(
                    formatQuantity(ingredientQuantity)
            );

            etExpiryDate.setText(
                    ingredientExpiry
            );

            setSpinnerSelection(
                    ingredientUnit
            );
        }
    }

    private void setSpinnerSelection(String unit) {

        if (unit == null) {
            return;
        }

        for (int i = 0; i < spinnerUnit.getCount(); i++) {

            String spinnerItem =
                    spinnerUnit.getItemAtPosition(i)
                            .toString();

            if (spinnerItem.equals(unit)) {

                spinnerUnit.setSelection(i);

                break;
            }
        }
    }

    private void saveIngredient() {

        String ingredientName =
                etIngredientName.getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity.getText()
                        .toString()
                        .trim();

        String expiryDate =
                etExpiryDate.getText()
                        .toString()
                        .trim();

        // Validate ingredient name
        if (ingredientName.isEmpty()) {

            etIngredientName.setError(
                    "Please enter an ingredient name"
            );

            etIngredientName.requestFocus();
            return;
        }

        // Validate quantity
        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Please enter a quantity"
            );

            etQuantity.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity = Double.parseDouble(
                    quantityText
            );

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Please enter a valid quantity"
            );

            etQuantity.requestFocus();
            return;
        }

        // Quantity must be greater than zero
        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than zero"
            );

            etQuantity.requestFocus();
            return;
        }

        // Validate unit
        if (spinnerUnit.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Please select a unit",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String unit =
                spinnerUnit.getSelectedItem()
                        .toString();

        if (isEditMode) {

            updateIngredient(
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );

        } else {

            addIngredient(
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );
        }
    }

    private void addIngredient(
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate
    ) {

        Ingredient ingredient =
                new Ingredient(
                        ingredientName,
                        quantity,
                        unit,
                        expiryDate
                );

        long result =
                databaseHelper.addIngredient(
                        ingredient
                );

        if (result != -1) {

            Toast.makeText(
                    this,
                    "Ingredient saved successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not save ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void updateIngredient(
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate
    ) {

        Ingredient ingredient =
                new Ingredient(
                        ingredientId,
                        ingredientName,
                        quantity,
                        unit,
                        expiryDate
                );

        int updatedRows =
                databaseHelper.updateIngredient(
                        ingredient
                );

        if (updatedRows > 0) {

            Toast.makeText(
                    this,
                    "Ingredient updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not update ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private String formatQuantity(double quantity) {

        if (quantity == Math.floor(quantity)) {

            return String.valueOf(
                    (int) quantity
            );
        }

        return String.valueOf(
                quantity
        );
    }
}