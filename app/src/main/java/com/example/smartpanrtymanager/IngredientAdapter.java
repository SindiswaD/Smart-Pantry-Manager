package com.example.smartpantrymanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class IngredientAdapter
        extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private final Runnable onDataChanged;

    public IngredientAdapter(
            List<Ingredient> ingredientList,
            Runnable onDataChanged
    ) {

        this.ingredientList = ingredientList;
        this.onDataChanged = onDataChanged;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_ingredient,
                                parent,
                                false
                        );

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position
    ) {

        Ingredient ingredient =
                ingredientList.get(position);

        /*
         * Ingredient name.
         */
        holder.tvIngredientName.setText(
                ingredient.getName()
        );

        /*
         * Quantity and unit.
         */
        String quantityText =
                formatQuantity(
                        ingredient.getQuantity()
                )
                        + " "
                        + ingredient.getUnit();

        holder.tvIngredientDetails.setText(
                quantityText
        );

        /*
         * Display the expiry date and calculate
         * whether the ingredient is expired,
         * expiring today or expiring soon.
         */
        displayExpiryInformation(
                holder,
                ingredient.getExpiryDate()
        );

        /*
         * EDIT INGREDIENT
         */
        holder.btnEditIngredient.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            holder.itemView.getContext(),
                            AddEditIngredientActivity.class
                    );

            intent.putExtra(
                    "ingredient_id",
                    ingredient.getId()
            );

            intent.putExtra(
                    "ingredient_name",
                    ingredient.getName()
            );

            intent.putExtra(
                    "ingredient_quantity",
                    ingredient.getQuantity()
            );

            intent.putExtra(
                    "ingredient_unit",
                    ingredient.getUnit()
            );

            intent.putExtra(
                    "ingredient_expiry",
                    ingredient.getExpiryDate()
            );

            holder.itemView
                    .getContext()
                    .startActivity(intent);
        });

        /*
         * DELETE INGREDIENT
         */
        holder.btnDeleteIngredient.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(
                            holder.itemView.getContext()
                    );

            builder.setTitle(
                    "Delete Ingredient"
            );

            builder.setMessage(
                    "Are you sure you want to delete "
                            + ingredient.getName()
                            + "?"
            );

            builder.setPositiveButton(
                    "Delete",
                    (dialog, which) -> {

                        DatabaseHelper databaseHelper =
                                new DatabaseHelper(
                                        holder.itemView.getContext()
                                );

                        int deletedRows =
                                databaseHelper.deleteIngredient(
                                        ingredient.getId()
                                );

                        if (deletedRows > 0) {

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Ingredient deleted successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            if (onDataChanged != null) {

                                onDataChanged.run();
                            }

                        } else {

                            Toast.makeText(
                                    holder.itemView.getContext(),
                                    "Unable to delete ingredient",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );

            builder.setNegativeButton(
                    "Cancel",
                    null
            );

            builder.show();
        });
    }

    /*
     * Displays expiry information based on the
     * current date and the ingredient's saved
     * expiry date.
     */
    private void displayExpiryInformation(
            IngredientViewHolder holder,
            String expiryDate
    ) {

        /*
         * Reset the status first because
         * RecyclerView reuses card views.
         */
        holder.tvExpiryStatus.setVisibility(
                View.GONE
        );

        if (expiryDate == null
                || expiryDate.trim().isEmpty()) {

            holder.tvExpiryDate.setText(
                    "No expiry date"
            );

            return;
        }

        holder.tvExpiryDate.setText(
                "Expires: " + expiryDate
        );

        try {

            /*
             * The app currently stores dates
             * in formats such as 26/9/2026.
             */
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "d/M/yyyy"
                    );

            LocalDate expiry =
                    LocalDate.parse(
                            expiryDate.trim(),
                            formatter
                    );

            LocalDate today =
                    LocalDate.now();

            long daysUntilExpiry =
                    ChronoUnit.DAYS.between(
                            today,
                            expiry
                    );

            /*
             * The ingredient has already expired.
             */
            if (daysUntilExpiry < 0) {

                long daysExpired =
                        Math.abs(daysUntilExpiry);

                if (daysExpired == 1) {

                    holder.tvExpiryStatus.setText(
                            "EXPIRED YESTERDAY"
                    );

                } else {

                    holder.tvExpiryStatus.setText(
                            "EXPIRED "
                                    + daysExpired
                                    + " DAYS AGO"
                    );
                }

                holder.tvExpiryStatus.setVisibility(
                        View.VISIBLE
                );

                return;
            }

            /*
             * The ingredient expires today.
             */
            if (daysUntilExpiry == 0) {

                holder.tvExpiryStatus.setText(
                        "EXPIRES TODAY"
                );

                holder.tvExpiryStatus.setVisibility(
                        View.VISIBLE
                );

                return;
            }

            /*
             * The ingredient expires tomorrow.
             */
            if (daysUntilExpiry == 1) {

                holder.tvExpiryStatus.setText(
                        "EXPIRES TOMORROW"
                );

                holder.tvExpiryStatus.setVisibility(
                        View.VISIBLE
                );

                return;
            }

            /*
             * Ingredients expiring within the
             * next seven days are highlighted.
             */
            if (daysUntilExpiry <= 7) {

                holder.tvExpiryStatus.setText(
                        "EXPIRING IN "
                                + daysUntilExpiry
                                + " DAYS"
                );

                holder.tvExpiryStatus.setVisibility(
                        View.VISIBLE
                );
            }

        } catch (DateTimeParseException e) {

            /*
             * If an older saved date has an
             * unexpected format, keep displaying
             * the original date rather than
             * crashing the application.
             */
            holder.tvExpiryStatus.setVisibility(
                    View.GONE
            );
        }
    }

    /*
     * Prevent quantities such as 5.0 from
     * displaying unnecessary decimal places.
     */
    private String formatQuantity(
            double quantity
    ) {

        if (quantity
                == Math.floor(quantity)) {

            return String.valueOf(
                    (int) quantity
            );
        }

        return String.valueOf(
                quantity
        );
    }

    @Override
    public int getItemCount() {

        return ingredientList.size();
    }

    public void updateData(
            List<Ingredient> newIngredientList
    ) {

        ingredientList =
                newIngredientList;

        notifyDataSetChanged();
    }

    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvIngredientName;
        TextView tvIngredientDetails;
        TextView tvExpiryDate;
        TextView tvExpiryStatus;

        Button btnEditIngredient;
        Button btnDeleteIngredient;

        public IngredientViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvIngredientName =
                    itemView.findViewById(
                            R.id.tvIngredientName
                    );

            tvIngredientDetails =
                    itemView.findViewById(
                            R.id.tvIngredientDetails
                    );

            tvExpiryDate =
                    itemView.findViewById(
                            R.id.tvExpiryDate
                    );

            tvExpiryStatus =
                    itemView.findViewById(
                            R.id.tvExpiryStatus
                    );

            btnEditIngredient =
                    itemView.findViewById(
                            R.id.btnEditIngredient
                    );

            btnDeleteIngredient =
                    itemView.findViewById(
                            R.id.btnDeleteIngredient
                    );
        }
    }
}