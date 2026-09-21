package com.example.smartpantrymanager;

public class Recipe {

    private int id;
    private String name;
    private String instructions;

    /*
     * These fields are calculated when recipe
     * recommendations are created.
     *
     * They are not stored as columns in the
     * recipes database table.
     */
    private int matchedIngredientCount;
    private int totalIngredientCount;
    private String missingIngredients;
    private boolean canMakeNow;

    public Recipe() {
    }

    public Recipe(
            int id,
            String name,
            String instructions
    ) {

        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    public Recipe(
            String name,
            String instructions
    ) {

        this.name = name;
        this.instructions = instructions;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getInstructions() {

        return instructions;
    }

    public void setInstructions(
            String instructions
    ) {

        this.instructions = instructions;
    }

    public int getMatchedIngredientCount() {

        return matchedIngredientCount;
    }

    public void setMatchedIngredientCount(
            int matchedIngredientCount
    ) {

        this.matchedIngredientCount =
                matchedIngredientCount;
    }

    public int getTotalIngredientCount() {

        return totalIngredientCount;
    }

    public void setTotalIngredientCount(
            int totalIngredientCount
    ) {

        this.totalIngredientCount =
                totalIngredientCount;
    }

    public String getMissingIngredients() {

        return missingIngredients;
    }

    public void setMissingIngredients(
            String missingIngredients
    ) {

        this.missingIngredients =
                missingIngredients;
    }

    public boolean isCanMakeNow() {

        return canMakeNow;
    }

    public void setCanMakeNow(
            boolean canMakeNow
    ) {

        this.canMakeNow =
                canMakeNow;
    }

    /*
     * Returns the percentage of recipe
     * ingredient requirements currently
     * satisfied by the pantry.
     */
    public int getMatchPercentage() {

        if (totalIngredientCount == 0) {

            return 0;
        }

        return (int) Math.round(
                (
                        matchedIngredientCount
                                * 100.0
                )
                        / totalIngredientCount
        );
    }
}