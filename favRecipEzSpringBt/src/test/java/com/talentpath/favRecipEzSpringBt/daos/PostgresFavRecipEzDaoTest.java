package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.exceptions.NullDirectionException;
import com.talentpath.favRecipEzSpringBt.exceptions.NullDirectionFieldException;
import com.talentpath.favRecipEzSpringBt.models.Instruction;
import com.talentpath.favRecipEzSpringBt.models.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("daotesting")
public class PostgresFavRecipEzDaoTest {
    @Autowired
    PostgresDao daoToTest;

    @BeforeEach
    void setUp() {daoToTest.reset();}

    class SortByName implements Comparator<Recipe>
    {
        public int compare(Recipe a, Recipe b){
            return a.getTitle().compareToIgnoreCase(b.getTitle());
        }
    }

    //getAllRecipes returns all the recipes that are public
    @Test
    void getAllRecipes(){
        List<Recipe> allRecipes = daoToTest.getAllRecipes();
        assertEquals(1, allRecipes.size());
//        allRecipes.sort(Comparator.comparing(Recipe::getTitle));


        assertEquals("Chicken and Rice", allRecipes.get(0).getTitle());

        assertEquals("hearty and classic", allRecipes.get(0).getDescription());

        assertEquals("American", allRecipes.get(0).getCategory());

        assertEquals("main course", allRecipes.get(0).getFoodType());

        assertEquals(10, allRecipes.get(0).getPrepTime());

        assertEquals(25, allRecipes.get(0).getCookTime());


        assertEquals(220, allRecipes.get(0).getServings());


        assertEquals(true, allRecipes.get(0).isPublicRecipe());


        assertEquals(2, allRecipes.get(0).getUserID());


        assertEquals(300, allRecipes.get(0).getCalories());


        assertEquals(35, allRecipes.get(0).getTotalTime());

    }

    @Test
    void getRecipesByUser(){

            //arrange
            //act
            List<Recipe> recipesUser1 = daoToTest.getRecipesByUser(1);
            List<Recipe> recipesUser2 = daoToTest.getRecipesByUser(2);
            List<Recipe> recipesUser3 = daoToTest.getRecipesByUser(3);


            //assert
            assertEquals(1, recipesUser1.size());
            assertEquals(0, recipesUser3.size());

            assertEquals("Broccoli and Beef", recipesUser1.get(0).getTitle());
            assertEquals("Chicken and Rice", recipesUser2.get(0).getTitle());

    }

    //Golden Path Test
    @Test
    void addInstruction(){
        try{
            //Arrange
            Instruction toAdd = new Instruction();
            toAdd.setRecipeID(1);
            toAdd.setInstructionLine("Enjoy this delicious classic dish!");

            //Act
            Instruction returnedInstruction = daoToTest.addInstruction(toAdd);

            //Assert
            assertEquals(1, returnedInstruction.getRecipeID());
            assertEquals("Enjoy this delicious classic dish!", returnedInstruction.getInstructionLine());
            assertEquals(3, returnedInstruction.getID());

        }catch(NullDirectionException | NullDirectionFieldException e){
            fail("produced invalid id exception during golden path test");
        }
    }

    @Test
    void addInstructionNullInstructionObject(){
        try{
            daoToTest.addInstruction(null);
            fail("should have caught NullDirectionException");
        }catch(NullDirectionException ex){

        }catch(Exception ex){
            fail("should not run into another exception besdies NullDirectionException.");
        }
    }

    @Test
    void addInstructionNullInstructionLine(){
        try{
            Instruction toAdd = new Instruction();
            toAdd.setRecipeID(1);
            toAdd.setInstructionLine(null);
            daoToTest.addInstruction(toAdd);
            fail("Should have caught NullDirectionFieldException. Instruction line was null.");
        }catch(NullDirectionFieldException ex){
            System.out.println("Failed correctly, called the null direction field exception.");
        }catch(Exception ex){
            fail("should not run into another exception besides nullDirectionException. "+  ex);
        }
    }

    @Test
    void addInstructionNullRecipeID(){
        try{
            Instruction toAdd = new Instruction();
            toAdd.setInstructionLine("Enjoy this delicious classic dish!");
            daoToTest.addInstruction(toAdd);
            fail("Should have caught NullDirectionFieldException. No recipe ID.");
        }catch(NullDirectionFieldException ex){

        }catch(Exception ex){
            fail("should not run into another exception besides nullDirectionException.");
        }
    }

}
