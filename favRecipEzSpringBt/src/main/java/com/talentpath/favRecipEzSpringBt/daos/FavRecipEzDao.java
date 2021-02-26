package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.exceptions.*;
import com.talentpath.favRecipEzSpringBt.models.Ingredient;
import com.talentpath.favRecipEzSpringBt.models.Instruction;
import com.talentpath.favRecipEzSpringBt.models.Recipe;

import java.util.List;

public interface FavRecipEzDao {
    List<Recipe> getAllRecipes();

    Recipe getRecipeByID(Integer id);

    Recipe addRecipe(Recipe recipe);

    int getUserIDByUsername(String username);

    List<Recipe> getRecipesByUser(Integer userID);

    Instruction addInstruction(Instruction instruction) throws NullDirectionException, NullDirectionFieldException;

    Ingredient addIngredient(Ingredient newIngred) throws NullIngredientException, NullIngredientFieldException;

    int deleteRecipe(Integer recipeID);

    int deleteRecipeInstructions(Integer recipeID);

    int deleteRecipeIngredients(Integer recipeID);

    List<Instruction> getInstByRecipeID(Integer recipeID);

    List<Ingredient> getIngredByRecipeID(Integer recipeID);

    List<Recipe> getAllPublicRecipesSearchTerm(String term);

    List<Recipe> getRecipesByUserSearchTerm(int userID, String term);

    void editRecipe(Integer recipeID, Recipe editedRecipe) throws FavRecipEzDaoException;

    void updateIngredient(Ingredient ingredient) throws FavRecipEzDaoException;

    void updateInstructions(Instruction instruction) throws FavRecipEzDaoException;

    int deleteInstruction(Integer instructionID);

    int deleteIngredient(Integer ingredientID);

    void reset();

}
