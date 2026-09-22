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
         * Calculate the pantry match percentage.
         */
        int matchPercentage =
                recipe.getMatchPercentage();

        /*
         * Display how many recipe ingredient
         * requirements are currently satisfied
         * by the pantry.
         */
        String matchText =
                recipe.getMatchedIngredientCount()
                        + " of "
                        + recipe.getTotalIngredientCount()
                        + " ingredients available • "
                        + matchPercentage
                        + "% match";

        holder.tvRecipeMatch.setText(
                matchText
        );

        /*
         * Decide which status should be shown
         * according to the pantry match.
         *
         * 100%      = Can Make Now
         * 50%-99%   = Almost There
         * 1%-49%    = Some Ingredients Available
         * 0%        = Ingredients Needed
         */
        if (recipe.isCanMakeNow()) {

            holder.tvRecipeStatus.setText(
                    "CAN MAKE NOW"
            );

            /*
             * A complete match means nothing
             * else is required.
             */
            holder.tvMissingIngredients.setVisibility(
                    View.GONE
            );

        } else {

            if (matchPercentage >= 50) {

                holder.tvRecipeStatus.setText(
                        "ALMOST THERE"
                );

            } else if (matchPercentage > 0) {

                holder.tvRecipeStatus.setText(
                        "SOME INGREDIENTS AVAILABLE"
                );

            } else {

                holder.tvRecipeStatus.setText(
                        "INGREDIENTS NEEDED"
                );
            }

            String missingIngredients =
                    recipe.getMissingIngredients();

            /*
             * "Need" is used instead of "Missing"
             * because an ingredient may exist in
             * the pantry but not in a sufficient
             * quantity.
             */
            if (missingIngredients != null
                    && !missingIngredients
                    .trim()
                    .isEmpty()) {

                holder.tvMissingIngredients.setText(
                        "Need: "
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
     * list and refresh the RecyclerView.
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