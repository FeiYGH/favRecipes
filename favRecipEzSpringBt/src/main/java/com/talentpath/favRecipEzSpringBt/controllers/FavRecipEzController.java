package com.talentpath.favRecipEzSpringBt.controllers;

import com.talentpath.favRecipEzSpringBt.exceptions.FavRecipEzDaoException;
import com.talentpath.favRecipEzSpringBt.models.Ingredient;
import com.talentpath.favRecipEzSpringBt.models.InstructAndIngredRequest;
import com.talentpath.favRecipEzSpringBt.models.Instruction;
import com.talentpath.favRecipEzSpringBt.models.Recipe;
import com.talentpath.favRecipEzSpringBt.services.favRecipEzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="http://localhost:4200")
public class FavRecipEzController {

    @Autowired
    favRecipEzService service;

    @GetMapping("/recipes")
    List<Recipe> getAllRecipes(){
        return service.getAllRecipes();
    }


    @GetMapping("/recipes/user")
    List<Recipe>getRecipesByUserID(Principal principal){
        List<Recipe> userRecipes = service.getRecipesByUser(principal.getName());
        return userRecipes;
    }

    @GetMapping("/recipesSearch")
    List<Recipe> searchRecipes(@RequestParam("title") String term, @RequestParam("allOrInd") String allOrInd, Principal principal ){
        List<Recipe> filteredRecipes;
        if(allOrInd.equals("all")){
            filteredRecipes = service.getRecipesAllBySearch(term);
            return filteredRecipes;
        }else{
            filteredRecipes = service.getRecipesUserBySearch(term, principal.getName());
            return filteredRecipes;
        }

    }


    @GetMapping("/recipes/{recipeId}")
    Recipe getRecipeByID(@PathVariable Integer recipeId){
        return service.getRecipeByID(recipeId);
    }


    @GetMapping("/recipeInstructions/{recipeID}")
    List<Instruction> getRecipeInstByID(@PathVariable Integer recipeID){
        return service.getRecipeInstByID(recipeID);
    }

    @GetMapping("/recipeIngredients/{recipeID}")
    List<Ingredient> getRecipeIngredByID(@PathVariable Integer recipeID){
        return service.getRecipeIngredByID(recipeID);
    }

    @PutMapping("/recipes/{recipeID}")
    public void editRecipe(@PathVariable Integer recipeID, @RequestBody Recipe editedRecipe) throws FavRecipEzDaoException {
        service.editRecipe(recipeID, editedRecipe);
    }


    @PostMapping("/recipes")
    public Recipe addRecipe(@RequestBody Recipe recipe, Principal principal) {
        return service.addRecipe(recipe, principal.getName());
    }

    @DeleteMapping("/recipes/{recipeID}")
        public int deleteRecipe(@PathVariable Integer recipeID){
            return service.deleteRecipe(recipeID);
        }

    @PostMapping("/recipeInstructions")
    public List<Instruction> addInstructionsToRecipe(@RequestBody InstructAndIngredRequest instructReq){
        return service.addRecipeInstructions(instructReq);
    }

    @PostMapping("/recipeInstructionsFromEdit")
    public List<Instruction> addInstructionsToRecipeFromEdit(@RequestBody List<Instruction> newInstructions){
        return service.addRecipeInstructionsFromEdit(newInstructions);
    }

    @PutMapping("/recipeInstructions")
    public void updateInstructionsForRecipe(@RequestBody List<Instruction> instructions) throws FavRecipEzDaoException{
        service.updateRecipeInstructions(instructions);
    }

    @DeleteMapping("/recipeInstructions/{recipeID}")
    public int deleteInstructionsForRecipe(@PathVariable Integer recipeID){
        return service.deleteRecipeInstructions(recipeID);
    }



    @PostMapping("/recipeIngredients")
    public List<Ingredient> addIngredientsToRecipe(@RequestBody InstructAndIngredRequest ingredReq){
        return service.addRecipeIngredients(ingredReq);
    }

    @PutMapping("/recipeIngredients")
    public void updateIngredientsToRecipe(@RequestBody List<Ingredient>ingredients) throws FavRecipEzDaoException{
        service.updateRecipeIngredients(ingredients);
    }

    @DeleteMapping("/recipeIngredients/{ingredientID}")
    public int deleteIngredientForRecipe(@PathVariable Integer ingredientID){
        String test = "it got here";
        int num = -7;
        return service.deleteRecipeIngredient(ingredientID);
    }

//    @PostMapping("/recipes")
//    Recipe addRecipe()
}
