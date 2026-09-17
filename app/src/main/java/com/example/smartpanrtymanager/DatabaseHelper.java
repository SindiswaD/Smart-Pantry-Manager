package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    // -------------------------------------------------
    // PANTRY INGREDIENTS TABLE
    // -------------------------------------------------

    private static final String TABLE_INGREDIENTS = "ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // -------------------------------------------------
    // RECIPES TABLE
    // -------------------------------------------------

    private static final String TABLE_RECIPES = "recipes";

    private static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_INSTRUCTIONS = "instructions";

    // -------------------------------------------------
    // RECIPE INGREDIENTS TABLE
    // -------------------------------------------------

    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    private static final String COLUMN_RECIPE_INGREDIENT_ID =
            "recipe_ingredient_id";

    private static final String COLUMN_INGREDIENT_NAME =
            "ingredient_name";

    private static final String COLUMN_REQUIRED_QUANTITY =
            "required_quantity";

    private static final String COLUMN_REQUIRED_UNIT =
            "required_unit";

    public DatabaseHelper(Context context) {

        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );

        // Open the database and make sure the
        // default recipe collection exists.
        SQLiteDatabase db = getWritableDatabase();

        seedRecipesIfNeeded(db);
    }

    // -------------------------------------------------
    // DATABASE CREATION
    // -------------------------------------------------

    @Override
    public void onCreate(SQLiteDatabase db) {

        createIngredientsTable(db);
        createRecipesTable(db);
        createRecipeIngredientsTable(db);
    }

    private void createIngredientsTable(
            SQLiteDatabase db
    ) {

        String createIngredientsTable =
                "CREATE TABLE " +
                        TABLE_INGREDIENTS +
                        " (" +
                        COLUMN_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME +
                        " TEXT NOT NULL, " +
                        COLUMN_QUANTITY +
                        " REAL NOT NULL, " +
                        COLUMN_UNIT +
                        " TEXT NOT NULL, " +
                        COLUMN_EXPIRY_DATE +
                        " TEXT" +
                        ")";

        db.execSQL(createIngredientsTable);
    }

    private void createRecipesTable(
            SQLiteDatabase db
    ) {

        String createRecipesTable =
                "CREATE TABLE " +
                        TABLE_RECIPES +
                        " (" +
                        COLUMN_RECIPE_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_NAME +
                        " TEXT NOT NULL, " +
                        COLUMN_INSTRUCTIONS +
                        " TEXT NOT NULL" +
                        ")";

        db.execSQL(createRecipesTable);
    }

    private void createRecipeIngredientsTable(
            SQLiteDatabase db
    ) {

        String createRecipeIngredientsTable =
                "CREATE TABLE " +
                        TABLE_RECIPE_INGREDIENTS +
                        " (" +
                        COLUMN_RECIPE_INGREDIENT_ID +
                        " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RECIPE_ID +
                        " INTEGER NOT NULL, " +
                        COLUMN_INGREDIENT_NAME +
                        " TEXT NOT NULL, " +
                        COLUMN_REQUIRED_QUANTITY +
                        " REAL NOT NULL, " +
                        COLUMN_REQUIRED_UNIT +
                        " TEXT NOT NULL, " +
                        "FOREIGN KEY(" +
                        COLUMN_RECIPE_ID +
                        ") REFERENCES " +
                        TABLE_RECIPES +
                        "(" +
                        COLUMN_RECIPE_ID +
                        ") ON DELETE CASCADE" +
                        ")";

        db.execSQL(
                createRecipeIngredientsTable
        );
    }

    // -------------------------------------------------
    // DATABASE UPGRADE
    // -------------------------------------------------

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {

        if (oldVersion < 2) {

            createRecipesTable(db);
            createRecipeIngredientsTable(db);
        }
    }

    // -------------------------------------------------
    // PANTRY INGREDIENT CRUD
    // -------------------------------------------------

    // CREATE
    public long addIngredient(
            Ingredient ingredient
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                ingredient.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                ingredient.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                ingredient.getUnit()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                ingredient.getExpiryDate()
        );

        long result =
                db.insert(
                        TABLE_INGREDIENTS,
                        null,
                        values
                );

        db.close();

        return result;
    }

    // READ ALL
    public List<Ingredient> getAllIngredients() {

        List<Ingredient> ingredientList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_INGREDIENTS,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_NAME
                                )
                        );

                double quantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_UNIT
                                )
                        );

                String expiryDate =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_EXPIRY_DATE
                                )
                        );

                Ingredient ingredient =
                        new Ingredient(
                                id,
                                name,
                                quantity,
                                unit,
                                expiryDate
                        );

                ingredientList.add(
                        ingredient
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return ingredientList;
    }

    // UPDATE
    public int updateIngredient(
            Ingredient ingredient
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_NAME,
                ingredient.getName()
        );

        values.put(
                COLUMN_QUANTITY,
                ingredient.getQuantity()
        );

        values.put(
                COLUMN_UNIT,
                ingredient.getUnit()
        );

        values.put(
                COLUMN_EXPIRY_DATE,
                ingredient.getExpiryDate()
        );

        int rowsUpdated =
                db.update(
                        TABLE_INGREDIENTS,
                        values,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(
                                        ingredient.getId()
                                )
                        }
                );

        db.close();

        return rowsUpdated;
    }

    // DELETE
    public int deleteIngredient(
            int ingredientId
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        int rowsDeleted =
                db.delete(
                        TABLE_INGREDIENTS,
                        COLUMN_ID + " = ?",
                        new String[]{
                                String.valueOf(
                                        ingredientId
                                )
                        }
                );

        db.close();

        return rowsDeleted;
    }

    // -------------------------------------------------
    // RECIPE DATABASE METHODS
    // -------------------------------------------------

    public long addRecipe(
            Recipe recipe
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                recipe.getName()
        );

        values.put(
                COLUMN_INSTRUCTIONS,
                recipe.getInstructions()
        );

        long recipeId =
                db.insert(
                        TABLE_RECIPES,
                        null,
                        values
                );

        db.close();

        return recipeId;
    }

    public long addRecipeIngredient(
            RecipeIngredient recipeIngredient
    ) {

        SQLiteDatabase db =
                getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_ID,
                recipeIngredient.getRecipeId()
        );

        values.put(
                COLUMN_INGREDIENT_NAME,
                recipeIngredient.getIngredientName()
        );

        values.put(
                COLUMN_REQUIRED_QUANTITY,
                recipeIngredient.getRequiredQuantity()
        );

        values.put(
                COLUMN_REQUIRED_UNIT,
                recipeIngredient.getUnit()
        );

        long result =
                db.insert(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        values
                );

        db.close();

        return result;
    }

    public List<Recipe> getAllRecipes() {

        List<Recipe> recipeList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPES,
                        null,
                        null,
                        null,
                        null,
                        null,
                        COLUMN_RECIPE_NAME + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String name =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_NAME
                                )
                        );

                String instructions =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_INSTRUCTIONS
                                )
                        );

                Recipe recipe =
                        new Recipe(
                                id,
                                name,
                                instructions
                        );

                recipeList.add(
                        recipe
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipeList;
    }

    public List<RecipeIngredient> getRecipeIngredients(
            int recipeId
    ) {

        List<RecipeIngredient> recipeIngredientList =
                new ArrayList<>();

        SQLiteDatabase db =
                getReadableDatabase();

        Cursor cursor =
                db.query(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        COLUMN_RECIPE_ID + " = ?",
                        new String[]{
                                String.valueOf(
                                        recipeId
                                )
                        },
                        null,
                        null,
                        COLUMN_RECIPE_INGREDIENT_ID + " ASC"
                );

        if (cursor.moveToFirst()) {

            do {

                int id =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_INGREDIENT_ID
                                )
                        );

                int storedRecipeId =
                        cursor.getInt(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_RECIPE_ID
                                )
                        );

                String ingredientName =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_INGREDIENT_NAME
                                )
                        );

                double requiredQuantity =
                        cursor.getDouble(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_QUANTITY
                                )
                        );

                String unit =
                        cursor.getString(
                                cursor.getColumnIndexOrThrow(
                                        COLUMN_REQUIRED_UNIT
                                )
                        );

                RecipeIngredient recipeIngredient =
                        new RecipeIngredient(
                                id,
                                storedRecipeId,
                                ingredientName,
                                requiredQuantity,
                                unit
                        );

                recipeIngredientList.add(
                        recipeIngredient
                );

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recipeIngredientList;
    }

    // -------------------------------------------------
    // DEFAULT RECIPE DATA
    // -------------------------------------------------

    private void seedRecipesIfNeeded(
            SQLiteDatabase db
    ) {

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM " +
                                TABLE_RECIPES,
                        null
                );

        int recipeCount = 0;

        if (cursor.moveToFirst()) {

            recipeCount =
                    cursor.getInt(0);
        }

        cursor.close();

        if (recipeCount > 0) {
            return;
        }

        db.beginTransaction();

        try {

            seedScrambledEggs(db);
            seedCheeseOmelette(db);
            seedFrenchToast(db);
            seedGrilledCheese(db);
            seedTomatoPasta(db);
            seedGarlicButterPasta(db);
            seedChickenAndRice(db);
            seedChickenPasta(db);
            seedEggFriedRice(db);
            seedTunaSandwich(db);
            seedChickenSandwich(db);
            seedMashedPotatoes(db);
            seedRoastedPotatoes(db);
            seedTomatoEggScramble(db);
            seedBananaPancakes(db);
            seedBasicPancakes(db);
            seedCheesePasta(db);
            seedChickenFriedRice(db);
            seedTomatoRice(db);
            seedTunaPasta(db);

            db.setTransactionSuccessful();

        } finally {

            db.endTransaction();
        }
    }

    // -------------------------------------------------
    // RECIPE 1
    // -------------------------------------------------

    private void seedScrambledEggs(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Scrambled Eggs",
                        "1. Crack the eggs into a bowl.\n" +
                                "2. Add the milk and whisk until combined.\n" +
                                "3. Melt the butter in a pan over medium heat.\n" +
                                "4. Pour in the egg mixture.\n" +
                                "5. Stir gently until the eggs are cooked and soft.\n" +
                                "6. Serve immediately."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                50,
                "ml"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                10,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 2
    // -------------------------------------------------

    private void seedCheeseOmelette(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Cheese Omelette",
                        "1. Crack the eggs into a bowl and whisk well.\n" +
                                "2. Melt the butter in a frying pan.\n" +
                                "3. Pour the eggs into the pan.\n" +
                                "4. Sprinkle the cheese over one half of the omelette.\n" +
                                "5. Fold the omelette over the cheese.\n" +
                                "6. Cook until the cheese has melted and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Cheese",
                40,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                10,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 3
    // -------------------------------------------------

    private void seedFrenchToast(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "French Toast",
                        "1. Whisk the eggs and milk together in a bowl.\n" +
                                "2. Dip each slice of bread into the egg mixture.\n" +
                                "3. Melt the butter in a frying pan.\n" +
                                "4. Fry the bread until golden on both sides.\n" +
                                "5. Serve while warm."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Bread",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                100,
                "ml"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                10,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 4
    // -------------------------------------------------

    private void seedGrilledCheese(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Grilled Cheese Sandwich",
                        "1. Place the cheese between two slices of bread.\n" +
                                "2. Spread butter on the outside of the bread.\n" +
                                "3. Heat a frying pan over medium heat.\n" +
                                "4. Cook the sandwich until golden on one side.\n" +
                                "5. Turn it over and cook the other side until the cheese melts."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Bread",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Cheese",
                50,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                10,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 5
    // -------------------------------------------------

    private void seedTomatoPasta(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Tomato Pasta",
                        "1. Cook the pasta according to the packet instructions.\n" +
                                "2. Heat the oil in a pan.\n" +
                                "3. Add the garlic and cook briefly.\n" +
                                "4. Add the tomatoes and cook until softened.\n" +
                                "5. Add the cooked pasta and mix well.\n" +
                                "6. Serve while hot."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tomatoes",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Garlic",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                15,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 6
    // -------------------------------------------------

    private void seedGarlicButterPasta(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Garlic Butter Pasta",
                        "1. Cook the pasta until tender and drain it.\n" +
                                "2. Melt the butter in a pan.\n" +
                                "3. Add the garlic and cook briefly.\n" +
                                "4. Add the cooked pasta to the pan.\n" +
                                "5. Toss until the pasta is coated in garlic butter.\n" +
                                "6. Serve immediately."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                30,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Garlic",
                2,
                "Items"
        );
    }

    // -------------------------------------------------
    // RECIPE 7
    // -------------------------------------------------

    private void seedChickenAndRice(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Chicken and Rice",
                        "1. Cook the rice until tender.\n" +
                                "2. Cut the chicken into small pieces.\n" +
                                "3. Heat the oil in a pan.\n" +
                                "4. Cook the chicken thoroughly.\n" +
                                "5. Add the cooked rice to the chicken.\n" +
                                "6. Mix together and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Rice",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                15,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 8
    // -------------------------------------------------

    private void seedChickenPasta(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Chicken Pasta",
                        "1. Cook the pasta and drain it.\n" +
                                "2. Cut the chicken into small pieces.\n" +
                                "3. Cook the chicken in a pan until fully cooked.\n" +
                                "4. Add the tomatoes and cook until softened.\n" +
                                "5. Add the pasta and mix everything together.\n" +
                                "6. Serve while hot."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tomatoes",
                2,
                "Items"
        );
    }

    // -------------------------------------------------
    // RECIPE 9
    // -------------------------------------------------

    private void seedEggFriedRice(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Egg Fried Rice",
                        "1. Cook the rice and allow it to cool slightly.\n" +
                                "2. Heat the oil in a frying pan.\n" +
                                "3. Crack the eggs into the pan and scramble them.\n" +
                                "4. Add the rice.\n" +
                                "5. Stir-fry until everything is heated through.\n" +
                                "6. Serve immediately."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Rice",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                15,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 10
    // -------------------------------------------------

    private void seedTunaSandwich(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Tuna Sandwich",
                        "1. Place the tuna in a bowl.\n" +
                                "2. Add the mayonnaise and mix well.\n" +
                                "3. Spread the tuna mixture over one slice of bread.\n" +
                                "4. Place the second slice of bread on top.\n" +
                                "5. Cut the sandwich and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Bread",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tuna",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Mayonnaise",
                20,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 11
    // -------------------------------------------------

    private void seedChickenSandwich(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Chicken Sandwich",
                        "1. Cook the chicken thoroughly and allow it to cool slightly.\n" +
                                "2. Cut or shred the chicken into small pieces.\n" +
                                "3. Mix the chicken with mayonnaise.\n" +
                                "4. Place the mixture onto one slice of bread.\n" +
                                "5. Top with the second slice and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Bread",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Mayonnaise",
                20,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 12
    // -------------------------------------------------

    private void seedMashedPotatoes(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Mashed Potatoes",
                        "1. Peel and cut the potatoes into pieces.\n" +
                                "2. Boil the potatoes until soft.\n" +
                                "3. Drain the water.\n" +
                                "4. Add the milk and butter.\n" +
                                "5. Mash until smooth and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Potatoes",
                500,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                100,
                "ml"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                30,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 13
    // -------------------------------------------------

    private void seedRoastedPotatoes(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Roasted Potatoes",
                        "1. Preheat the oven.\n" +
                                "2. Cut the potatoes into evenly sized pieces.\n" +
                                "3. Coat the potatoes with oil.\n" +
                                "4. Place them on a baking tray.\n" +
                                "5. Roast until golden and tender.\n" +
                                "6. Serve while hot."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Potatoes",
                500,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                30,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 14
    // -------------------------------------------------

    private void seedTomatoEggScramble(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Tomato and Egg Scramble",
                        "1. Chop the tomatoes.\n" +
                                "2. Heat the oil in a frying pan.\n" +
                                "3. Cook the tomatoes until slightly softened.\n" +
                                "4. Crack the eggs into the pan.\n" +
                                "5. Stir gently until the eggs are cooked.\n" +
                                "6. Serve immediately."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tomatoes",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                10,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 15
    // -------------------------------------------------

    private void seedBananaPancakes(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Banana Pancakes",
                        "1. Mash the banana in a bowl.\n" +
                                "2. Add the eggs and mix well.\n" +
                                "3. Add the flour and milk and mix into a batter.\n" +
                                "4. Heat a frying pan.\n" +
                                "5. Spoon small amounts of batter into the pan.\n" +
                                "6. Cook each pancake on both sides until golden."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Banana",
                1,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Flour",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                100,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 16
    // -------------------------------------------------

    private void seedBasicPancakes(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Basic Pancakes",
                        "1. Add the flour to a mixing bowl.\n" +
                                "2. Add the eggs and milk.\n" +
                                "3. Mix until a smooth batter forms.\n" +
                                "4. Melt a small amount of butter in a frying pan.\n" +
                                "5. Pour some batter into the pan.\n" +
                                "6. Cook on both sides until golden."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Flour",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                200,
                "ml"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Butter",
                20,
                "g"
        );
    }

    // -------------------------------------------------
    // RECIPE 17
    // -------------------------------------------------

    private void seedCheesePasta(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Cheese Pasta",
                        "1. Cook the pasta until tender and drain it.\n" +
                                "2. Warm the milk gently in a pan.\n" +
                                "3. Add the cheese and stir until melted.\n" +
                                "4. Add the cooked pasta.\n" +
                                "5. Mix until the pasta is coated in the cheese sauce.\n" +
                                "6. Serve while hot."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Cheese",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Milk",
                150,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 18
    // -------------------------------------------------

    private void seedChickenFriedRice(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Chicken Fried Rice",
                        "1. Cook the rice and set it aside.\n" +
                                "2. Cut the chicken into small pieces.\n" +
                                "3. Heat the oil in a frying pan.\n" +
                                "4. Cook the chicken thoroughly.\n" +
                                "5. Add the eggs and scramble them with the chicken.\n" +
                                "6. Add the rice and stir-fry until heated through."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Chicken",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Rice",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Eggs",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                15,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 19
    // -------------------------------------------------

    private void seedTomatoRice(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Tomato Rice",
                        "1. Cook the rice until tender.\n" +
                                "2. Heat the oil in a pan.\n" +
                                "3. Add the garlic and cook briefly.\n" +
                                "4. Add the tomatoes and cook until soft.\n" +
                                "5. Add the cooked rice.\n" +
                                "6. Mix well and serve."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Rice",
                150,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tomatoes",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Garlic",
                2,
                "Items"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Oil",
                15,
                "ml"
        );
    }

    // -------------------------------------------------
    // RECIPE 20
    // -------------------------------------------------

    private void seedTunaPasta(
            SQLiteDatabase db
    ) {

        long recipeId =
                insertRecipe(
                        db,
                        "Tuna Pasta",
                        "1. Cook the pasta until tender and drain it.\n" +
                                "2. Place the tuna in a bowl.\n" +
                                "3. Add the mayonnaise and mix well.\n" +
                                "4. Add the cooked pasta.\n" +
                                "5. Mix until everything is evenly combined.\n" +
                                "6. Serve warm or cold."
                );

        insertRecipeIngredient(
                db,
                recipeId,
                "Pasta",
                200,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Tuna",
                100,
                "g"
        );

        insertRecipeIngredient(
                db,
                recipeId,
                "Mayonnaise",
                30,
                "g"
        );
    }

    // -------------------------------------------------
    // SEEDING HELPER METHODS
    // -------------------------------------------------

    private long insertRecipe(
            SQLiteDatabase db,
            String name,
            String instructions
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_NAME,
                name
        );

        values.put(
                COLUMN_INSTRUCTIONS,
                instructions
        );

        return db.insertOrThrow(
                TABLE_RECIPES,
                null,
                values
        );
    }

    private void insertRecipeIngredient(
            SQLiteDatabase db,
            long recipeId,
            String ingredientName,
            double requiredQuantity,
            String unit
    ) {

        ContentValues values =
                new ContentValues();

        values.put(
                COLUMN_RECIPE_ID,
                recipeId
        );

        values.put(
                COLUMN_INGREDIENT_NAME,
                ingredientName
        );

        values.put(
                COLUMN_REQUIRED_QUANTITY,
                requiredQuantity
        );

        values.put(
                COLUMN_REQUIRED_UNIT,
                unit
        );

        db.insertOrThrow(
                TABLE_RECIPE_INGREDIENTS,
                null,
                values
        );
    }
}