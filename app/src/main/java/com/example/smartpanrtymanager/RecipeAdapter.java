package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private final OnRecipeClickListener listener;

    /*
     * Allows the Activity to know which
     * recipe the user selected.
     */
    public interface OnRecipeClickListener {

        void onRecipeClick(Recipe recipe);
    }

    public RecipeAdapter(
            List<Recipe> recipeList,
            OnRecipeClickListener listener
    ) {

        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(
                                R.layout.item_recipe,
                                parent,
                                false
                        );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position
    ) {

        Recipe recipe =
                recipeList.get(position);

        /*
         * Display the basic recipe information.
         */
        holder.tvRecipeName.setText(
                recipe.getName()
        );

        holder.tvRecipeInstructions.setText(
                recipe.getInstructions()
        );

        /*
         * Display how many of the recipe's
         * ingredient requirements are currently
         * satisfied by the pantry.
         */
        String matchText =
                recipe.getMatchedIngredientCount()
                        + " of "
                        + recipe.getTotalIngredientCount()
                        + " ingredients available • "
                        + recipe.getMatchPercentage()
                        + "% match";

        holder.tvRecipeMatch.setText(
                matchText
        );

        /*
         * A complete match means the user
         * currently has every required ingredient
         * in a sufficient quantity.
         */
        if (recipe.isCanMakeNow()) {

            holder.tvRecipeStatus.setText(
                    "CAN MAKE NOW"
            );

            /*
             * There is nothing missing, so this
             * TextView should not take up space.
             */
            holder.tvMissingIngredients.setVisibility(
                    View.GONE
            );

        } else {

            holder.tvRecipeStatus.setText(
                    "ALMOST THERE"
            );

            String missingIngredients =
                    recipe.getMissingIngredients();

            if (missingIngredients != null
                    && !missingIngredients
                    .trim()
                    .isEmpty()) {

                holder.tvMissingIngredients.setText(
                        "Missing: "
                                + missingIngredients
                );

                holder.tvMissingIngredients.setVisibility(
                        View.VISIBLE
                );

            } else {

                holder.tvMissingIngredients.setVisibility(
                        View.GONE
                );
            }
        }

        /*
         * Open the selected recipe when
         * the user taps its card.
         */
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {

                listener.onRecipeClick(
                        recipe
                );
            }
        });
    }

    @Override
    public int getItemCount() {

        return recipeList.size();
    }

    /*
     * Replace the currently displayed recipe
     * recommendations and refresh the RecyclerView.
     */
    public void updateData(
            List<Recipe> newRecipeList
    ) {

        recipeList = newRecipeList;

        notifyDataSetChanged();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeStatus;
        TextView tvRecipeMatch;
        TextView tvMissingIngredients;
        TextView tvRecipeInstructions;

        public RecipeViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );

            tvRecipeStatus =
                    itemView.findViewById(
                            R.id.tvRecipeStatus
                    );

            tvRecipeMatch =
                    itemView.findViewById(
                            R.id.tvRecipeMatch
                    );

            tvMissingIngredients =
                    itemView.findViewById(
                            R.id.tvMissingIngredients
                    );

            tvRecipeInstructions =
                    itemView.findViewById(
                            R.id.tvRecipeInstructions
                    );
        }
    }
}