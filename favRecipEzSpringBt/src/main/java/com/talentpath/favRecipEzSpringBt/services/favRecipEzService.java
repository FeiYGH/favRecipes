package com.talentpath.favRecipEzSpringBt.services;

import com.talentpath.favRecipEzSpringBt.daos.FavRecipEzDao;
import com.talentpath.favRecipEzSpringBt.daos.UserRepository;
import com.talentpath.favRecipEzSpringBt.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class favRecipEzService {
    @Autowired
    FavRecipEzDao dao;

    @Autowired
    UserRepository userRepo;

    public List<Recipe> getAllRecipes() {
        return dao.getAllRecipes();
    }

    public List<Recipe> getRecipesByUser(String username) {

        Optional<User> userIfExists = userRepo.findByUsername(username);
        try{
            //userIfExists is Optional object, .get() to get the value
            int userID = userIfExists.get().getId();
            return dao.getRecipesByUser(userID);
        }catch(NoSuchElementException ex){
            System.out.println("No user with that username exists. " + ex);
        }
        return null;
    }

    public Recipe getRecipeByID(Integer id) {
        return dao.getRecipeByID(id);
    }

    public Recipe addRecipe(Recipe recipe, String username) {
        Optional<User> userIfExists = userRepo.findByUsername(username);
        try{
            int userID = userIfExists.get().getId();
            recipe.setUserID(userID);
            return dao.addRecipe(recipe);
        }catch(NoSuchElementException ex){
            System.out.println("No user with that username exists. " + ex);
        }

        return null;
    }

    private int getUserID(String username) {
        return dao.getUserIDByUsername(username);
    }


    public List<Instruction> addRecipeInstructions(InstructAndIngredRequest instructReq) {
        List<Instruction> newInstructions = new ArrayList<>();
        int recipeID = instructReq.getRecipeID();
        String[] instructions = instructReq.getIngredOrInstructLines();
        for(String line : instructions){
            line = line.trim();
            if(line.length() > 0) {
                Instruction newInstruct = new Instruction(recipeID, line);
                newInstructions.add(dao.addInstruction(newInstruct));
            }
        }
        return newInstructions;
    }

    public List<Ingredient> addRecipeIngredients(InstructAndIngredRequest ingredReq) {
        List<Ingredient> newIngredients = new ArrayList<>();
        int recipeID = ingredReq.getRecipeID();
        String[] ingredients = ingredReq.getIngredOrInstructLines();
        for(String line : ingredients){
            line = line.trim();
            if(line.length() > 0) {
                Ingredient newIngred = new Ingredient(recipeID, line);
                newIngredients.add(dao.addIngredient(newIngred));
            }
        }
        return newIngredients;
    }

    public int deleteRecipe(Integer recipeID) {
         return dao.deleteRecipe(recipeID);
    }

    public int deleteRecipeInstructions(Integer recipeID) {
        return dao.deleteRecipeInstructions(recipeID);
    }

    public int deleteRecipeIngredients(Integer recipeID) {
        return dao.deleteRecipeIngredients(recipeID);
    }

    public List<Instruction> getRecipeInstByID(Integer recipeID) {
        return dao.getInstByRecipeID(recipeID);
    }

    public List<Ingredient> getRecipeIngredByID(Integer recipeID) {
        return dao.getIngredByRecipeID(recipeID);
    }

    public List<Recipe> getRecipesAllBySearch(String term) {
        return dao.getAllPublicRecipesSearchTerm(term);
    }


    public List<Recipe> getRecipesUserBySearch(String term, String username) {
        Optional<User> userIfExists = userRepo.findByUsername(username);
        try{
            //userIfExists is Optional object, .get() to get the value
            int userID = userIfExists.get().getId();
            return dao.getRecipesByUserSearchTerm(userID, term);
        }catch(NoSuchElementException ex){
            System.out.println("No user with that username exists. " + ex);
        }
        return null;
    }
}
