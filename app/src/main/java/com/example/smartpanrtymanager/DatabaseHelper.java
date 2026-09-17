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

    // INGREDIENTS TABLE
    private static final String TABLE_INGREDIENTS = "ingredients";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT = "unit";
    private static final String COLUMN_EXPIRY_DATE = "expiry_date";

    // RECIPES TABLE
    private static final String TABLE_RECIPES = "recipes";

    private static final String COLUMN_RECIPE_ID = "recipe_id";
    private static final String COLUMN_RECIPE_NAME = "recipe_name";
    private static final String COLUMN_INSTRUCTIONS = "instructions";

    // RECIPE INGREDIENTS TABLE
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
    }

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

    public List<RecipeIngredient>
    getRecipeIngredients(
            int recipeId
    ) {

        List<RecipeIngredient>
                recipeIngredientList =
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
                        COLUMN_RECIPE_INGREDIENT_ID +
                                " ASC"
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

                RecipeIngredient
                        recipeIngredient =
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
}