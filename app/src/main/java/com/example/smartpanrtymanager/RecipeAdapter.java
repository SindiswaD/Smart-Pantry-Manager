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
     * This interface allows the Activity to know
     * which recipe the user selected.
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

        holder.tvRecipeName.setText(
                recipe.getName()
        );

        holder.tvRecipeInstructions.setText(
                recipe.getInstructions()
        );

        /*
         * Open the selected recipe when
         * the user taps its card.
         */
        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {

                listener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {

        return recipeList.size();
    }

    public void updateData(
            List<Recipe> newRecipeList
    ) {

        recipeList = newRecipeList;

        notifyDataSetChanged();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvRecipeName;
        TextView tvRecipeInstructions;

        public RecipeViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvRecipeName =
                    itemView.findViewById(
                            R.id.tvRecipeName
                    );

            tvRecipeInstructions =
                    itemView.findViewById(
                            R.id.tvRecipeInstructions
                    );
        }
    }
}