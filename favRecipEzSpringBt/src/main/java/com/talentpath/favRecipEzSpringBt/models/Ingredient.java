package com.talentpath.favRecipEzSpringBt.models;

public class Ingredient {
    int ID;
    String ingredientStr;
    Integer recipeID;

    public Ingredient(){};

    public Ingredient(Integer recipeID, String ingredientStr){
        this.recipeID = recipeID;
        this.ingredientStr = ingredientStr;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIngredientStr() {
        return ingredientStr;
    }

    public void setIngredientStr(String ingredientStr) {
        this.ingredientStr = ingredientStr;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }
};
