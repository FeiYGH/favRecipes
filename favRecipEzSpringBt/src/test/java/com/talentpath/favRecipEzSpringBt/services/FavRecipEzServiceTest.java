package com.talentpath.favRecipEzSpringBt.services;


import com.talentpath.favRecipEzSpringBt.daos.FavRecipEzDao;
import com.talentpath.favRecipEzSpringBt.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"servicetesting","daotesting"})
public class FavRecipEzServiceTest {
    @Autowired
    FavRecipEzDao dao;

    @Autowired
    favRecipEzService serviceToTest;

    @BeforeEach
    void setUp(){
        dao.reset();
    }

    @Test
    void testing(){
        System.out.println("test worked");
    }

    //golden path test for getting current public recipes
    @Test
    void getAllRecipes(){
        //Arrange
        //Act
        List<Recipe> allPublicRecipes = serviceToTest.getAllRecipes();
        //Assert
        assertEquals(1, allPublicRecipes.size());
    }

    //golden path test for adding a recipe by user
    void addRecipe(){
        //Arrange
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.setTitle("recipeToAdd1");
        recipeToAdd.setDescription("descriptionToAdd1");
        recipeToAdd.setCategory("Chinese");
        recipeToAdd.setFoodType("dessert");
        recipeToAdd.setPrepTime(20);
        recipeToAdd.setCookTime(35);
        recipeToAdd.setServings(4);
        recipeToAdd.setPublicRecipe(true);
        recipeToAdd.setUserID(2);
        recipeToAdd.setCalories(400);
        recipeToAdd.setTotalTime(55);
        //Act
        try{
            Recipe addedRecipe = serviceToTest.addRecipe(recipeToAdd, "user1");
            assertEquals(3, addedRecipe.getId());
            assertEquals("recipeToAdd1", addedRecipe.getTitle());
            assertEquals("descriptionToAdd1", addedRecipe.getDescription());
            assertEquals("Chinese", addedRecipe.getCategory());
            assertEquals("dessert", addedRecipe.getFoodType());
            assertEquals(20, addedRecipe.getPrepTime());
            assertEquals(35, addedRecipe.getCookTime());
            assertEquals(4, addedRecipe.getServings());
            assertEquals(true, addedRecipe.isPublicRecipe());
            assertEquals(2, addedRecipe.getUserID());
            assertEquals(400, addedRecipe.getCalories());
            assertEquals(55, addedRecipe.getTotalTime());

        }catch(NoSuchElementException ex){
            fail("Golden path test for addRecipe. Should not have triggered NoSuchElementException");
        }
        List<Recipe> allPublicRecipes = serviceToTest.getAllRecipes();
        assertEquals(2, allPublicRecipes.size());
    }

    @Test
    void addRecipeNonExistentUser(){
        //Arrange
        Recipe recipeToAdd = new Recipe();
        recipeToAdd.setTitle("recipeToAdd1");
        recipeToAdd.setDescription("descriptionToAdd1");
        recipeToAdd.setCategory("Chinese");
        recipeToAdd.setFoodType("dessert");
        recipeToAdd.setPrepTime(20);
        recipeToAdd.setCookTime(35);
        recipeToAdd.setServings(4);
        recipeToAdd.setPublicRecipe(true);
        recipeToAdd.setUserID(2);
        recipeToAdd.setCalories(400);
        recipeToAdd.setTotalTime(55);
        //Act
        try{


            serviceToTest.addRecipe(recipeToAdd, "nonExistentUser");
            fail("In addRecipeNonExistentUser, should have caught NoSuchElement exception");
        }catch(Exception ex){
            System.out.println("Correct, no user with that username");
        }

    }






}
