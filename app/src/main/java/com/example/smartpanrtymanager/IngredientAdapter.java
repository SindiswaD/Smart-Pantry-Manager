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

        View view = LayoutInflater
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

        holder.tvIngredientName.setText(
                ingredient.getName()
        );

        String quantityText =
                formatQuantity(
                        ingredient.getQuantity()
                )
                        + " "
                        + ingredient.getUnit();

        holder.tvIngredientDetails.setText(
                quantityText
        );

        String expiryDate =
                ingredient.getExpiryDate();

        if (expiryDate == null ||
                expiryDate.isEmpty()) {

            holder.tvExpiryDate.setText(
                    "No expiry date"
            );

        } else {

            holder.tvExpiryDate.setText(
                    "Expires: " + expiryDate
            );
        }

        // EDIT
        holder.btnEditIngredient.setOnClickListener(v -> {

            Intent intent = new Intent(
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

        // DELETE
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