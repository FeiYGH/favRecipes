package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.exceptions.*;
import com.talentpath.favRecipEzSpringBt.models.Ingredient;
import com.talentpath.favRecipEzSpringBt.models.Instruction;
import com.talentpath.favRecipEzSpringBt.models.Recipe;
import com.talentpath.favRecipEzSpringBt.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sound.midi.Soundbank;
import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile({"production", "daotesting"})
public class PostgresDao implements FavRecipEzDao {
    @Autowired
    private JdbcTemplate template;

    @Override
    public List<Recipe> getAllRecipes() {
        //getting all recipes that are public
        List<Recipe> allRecipes = template.query("SELECT * FROM public.\"Recipes\" WHERE \"publicRecipe\"=true;", new RecipeMapper());
        //System.out.println("got here");
        return allRecipes;
    }

    @Override
    public List<Recipe> getRecipesByUser(Integer userID) {
        List<Recipe> recipesByUser = template.query("SELECT *\n" +
                "\tFROM public.\"Recipes\" \n" +
                "\tWHERE \"userID\" = "+userID+";", new RecipeMapper());
        //System.out.println("got here");
        return recipesByUser;
    }

    @Override
    public Instruction addInstruction(Instruction newInstruct) throws NullDirectionException, NullDirectionFieldException {
      if(newInstruct == null) {
          throw new NullDirectionException("Received null for direction object.");
      }

      if(newInstruct.getInstructionLine()==null){
          throw new NullDirectionFieldException("Received null for direction line in direction object.");
      }

        if(newInstruct.getRecipeID()==null){
            throw new NullDirectionFieldException("Received null for recipeID in direction object.");
        }

        List<Integer> insertedIds = template.query("INSERT INTO public.\"Directions\"(\n" +
                "\t\"instructionLine\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";", new InstAndIngredIDMapper(), newInstruct.getInstructionLine(), newInstruct.getRecipeID());
        newInstruct.setID(insertedIds.get(0));
        return newInstruct;
    }


    @Override
    public Ingredient addIngredient(Ingredient newIngred) throws NullIngredientException, NullIngredientFieldException {
        if(newIngred == null) {
            throw new NullIngredientException("Received null for ingredient object.");
        }
        if(newIngred.getIngredientStr().equals(null)){
            throw new NullIngredientFieldException("Ingredient line cannot be null for ingredient object");
        }
        if(newIngred.getRecipeID()==null){
            throw new NullIngredientFieldException("Ingredient recipeID cannot be null for ingredient object.");
        }

        List<Integer> insertedIds = template.query("INSERT INTO public.\"Ingredients\"(\n" +
                "\t\"ingredientStr\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";",new InstAndIngredIDMapper(), newIngred.getIngredientStr(), newIngred.getRecipeID() );
        newIngred.setID(insertedIds.get(0));
        return newIngred;
    };

    @Override
    public int deleteRecipe(Integer recipeID) {
        int numRecipesDeleted = template.update("DELETE FROM public.\"Recipes\"\n" +
                "\tWHERE \"ID\"="+recipeID+";");
        String test = "gotHere";
        return numRecipesDeleted;
    }

    @Override
    public int deleteRecipeInstructions(Integer recipeID) {
        int deletedInstNumRows = template.update("DELETE FROM public.\"Directions\"\n" +
                "\tWHERE \"recipeID\" = "+recipeID+";");
        return deletedInstNumRows;
    }

    @Override
    public int deleteRecipeIngredients(Integer recipeID) {
        int deletedIngredNumRows = template.update("DELETE FROM public.\"Ingredients\"\n" +
                "\tWHERE \"recipeID\"="+recipeID+";");
        return deletedIngredNumRows;
    }

    @Override
    public List<Instruction> getInstByRecipeID(Integer recipeID) {
        List<Instruction> retrievedInstructions = template.query("SELECT * FROM public.\"Directions\" WHERE \"recipeID\"="+recipeID+";", new InstructionMapper());
        return retrievedInstructions;
    }

    @Override
    public List<Ingredient> getIngredByRecipeID(Integer recipeID) {
        List<Ingredient> retrievedIngredients = template.query("SELECT * FROM public.\"Ingredients\" WHERE \"recipeID\" = "+recipeID+"; ", new IngredientMapper());
        return retrievedIngredients;
    }

    @Override
    public List<Recipe> getAllPublicRecipesSearchTerm(String term) {
        List<Recipe> allRecipes = template.query("SELECT * FROM public.\"Recipes\" WHERE \"publicRecipe\"=true;", new RecipeMapper());
        //System.out.println("got here");
        String lowerCaseTerm = term;
        return allRecipes.stream().filter(recipe -> recipe.getTitle().toLowerCase().contains(lowerCaseTerm)).collect(Collectors.toList());
    }

    @Override
    public List<Recipe> getRecipesByUserSearchTerm(int userID, String term) {
        List<Recipe> recipesByUser = template.query("SELECT *\n" +
                "\tFROM public.\"Recipes\" \n" +
                "\tWHERE \"userID\" = "+userID+";", new RecipeMapper());
        String lowerCaseTerm = term;
        return recipesByUser.stream().filter(recipe -> recipe.getTitle().toLowerCase().contains(lowerCaseTerm)).collect(Collectors.toList());

    }

    @Override
    public void editRecipe(Integer recipeID, Recipe editedRecipe) throws FavRecipEzDaoException{
        try{
            int rowsAffected= template.update("UPDATE public.\"Recipes\"\n" +
                    "\tSET title=?, description=?, category=?, \"foodType\"=?, \"prepTime\"=?, \"cookTime\"=?, servings=?, \"publicRecipe\"=?, calories=?, \"totalTime\"=?\n" +
                    "\tWHERE \"ID\"=?", editedRecipe.getTitle(), editedRecipe.getDescription(), editedRecipe.getCategory(), editedRecipe.getFoodType(), editedRecipe.getPrepTime(), editedRecipe.getCookTime(), editedRecipe.getServings(), editedRecipe.isPublicRecipe(),editedRecipe.getCalories(), editedRecipe.getTotalTime(), recipeID);
        }catch(DataAccessException ex){
            throw new FavRecipEzDaoException("Error while trying to edit recipe: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateIngredient(Ingredient ingredient) throws FavRecipEzDaoException {
        try{
            int rowsAffected = template.update("UPDATE public.\"Ingredients\"\n" +
                    "\tSET \"ingredientStr\"=?\n" +
                    "\tWHERE \"ID\" = ?;", ingredient.getIngredientStr(), ingredient.getID());
        }catch(DataAccessException ex){
            throw new FavRecipEzDaoException("Error while updating ingredient to postgres: "+ ex.getMessage(),ex);
        }
    }



    @Override
    public void updateInstructions(Instruction instruction) throws FavRecipEzDaoException {
        try{
            int rowsAffected= template.update("UPDATE public.\"Directions\"\n" +
                    "\tSET \"instructionLine\"=?\n" +
                    "\tWHERE \"ID\"=?;", instruction.getInstructionLine(), instruction.getID());
        }catch(DataAccessException ex){
            throw new FavRecipEzDaoException("Error while updating instruction to postgres: " + ex.getMessage(), ex);
        }
    }

    @Override
    public int deleteInstruction(Integer instructionID) {
        int deletedInstructNumRows = template.update("DELETE FROM public.\"Directions\"\n" +
                "\tWHERE \"ID\"="+instructionID +";");
        return deletedInstructNumRows;
    }

    @Override
    public int deleteIngredient(Integer ingredientID) {
        int deletedIngredNumRows = template.update("DELETE FROM public.\"Ingredients\"\n" +
                "\tWHERE \"ID\"="+ingredientID+";");
        return deletedIngredNumRows;
    }

    @Override
    public void reset() {
        User user1 = new User("user1", "user1@gmail.com", "password");
        User user2 = new User("user2", "user2@gmail.com", "password");


        Ingredient newIngred1 = new Ingredient(1, "broccoli");
        Ingredient newIngred2 = new Ingredient(1, "beef");

        Instruction newInstruct1 = new Instruction(1, "Cut the broccoli into florets and cube the beef until they're one inch in diameter.");
        Instruction newInstruct2 = new Instruction(1, "Stir fry together in a big large saute pan with soy sauce, Charles' Worchester Sauce, and garlic.");

        //truncating tables
        template.update("TRUNCATE \"users\", \"user_roles\", \"roles\", \"Reviews\", \"ReviewReplies\", \"Recipes\", \"RecipePics\", \"RecipeLikes\", \"Ratings\", \"Ingredients\", \"Directions\"  RESTART IDENTITY");

        //inserting three users, user1, user17, user27
        template.update("INSERT INTO public.users(\n" +
                "\temail, password, profile_pic_url, username)\n" +
                "\tVALUES ('user1@gmail.com', 'password', 'https://www.user1ProfilePic.com', 'user1');");

        template.update("INSERT INTO public.users(\n" +
                "\temail, password, profile_pic_url, username)\n" +
                "\tVALUES ('user2@gmail.com', 'password', 'https://www.user2ProfilePic.com', 'user2');");

        template.update("INSERT INTO public.users(\n" +
                "\temail, password, profile_pic_url, username)\n" +
                "\tVALUES ('user3@gmail.com', 'password', 'https://www.user3ProfilePic.com', 'user3');");

        //inserting two recipes (Broccoli and Beef - user1, Chicken and Rice - user2)
        template.update("INSERT INTO public.\"Recipes\"(\n" +
                "\ttitle, description, category, \"foodType\", \"prepTime\", \"cookTime\", servings, \"publicRecipe\", \"userID\", calories, \"totalTime\")\n" +
                "\tVALUES ('Broccoli and Beef', 'classic Chinese dish', 'Chinese', 'main course', 10, 20, 200, False, 1, 320, 30);");

        template.update("INSERT INTO public.\"Recipes\"(\n" +
                "\ttitle, description, category, \"foodType\", \"prepTime\", \"cookTime\", servings, \"publicRecipe\", \"userID\", calories, \"totalTime\")\n" +
                "\tVALUES ('Chicken and Rice', 'hearty and classic', 'American', 'main course', 10, 25, 220, True, 2, 300, 35);");

        //inserting ingredients into "Broccoli and Beef" and grabbing the IDs from postgres
        List<Integer> insertedIds1 = template.query("INSERT INTO public.\"Ingredients\"(\n" +
                "\t\"ingredientStr\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";",new InstAndIngredIDMapper(), newIngred1.getIngredientStr(), newIngred1.getRecipeID() );

        newIngred1.setID(insertedIds1.get(0));

        List<Integer> insertedIds2 = template.query("INSERT INTO public.\"Ingredients\"(\n" +
                "\t\"ingredientStr\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";",new InstAndIngredIDMapper(), newIngred2.getIngredientStr(), newIngred2.getRecipeID() );

        newIngred1.setID(insertedIds2.get(0));

        //inserting directions into "Broccoli and Beef" and grabbing the IDs from postgres
        List<Integer> insertedIdsDir1 = template.query("INSERT INTO public.\"Directions\"(\n" +
                "\t\"instructionLine\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";", new InstAndIngredIDMapper(), newInstruct1.getInstructionLine(), newInstruct1.getRecipeID());
        newInstruct1.setID(insertedIdsDir1.get(0));

        List<Integer> insertedIdsDir2 = template.query("INSERT INTO public.\"Directions\"(\n" +
                "\t\"instructionLine\", \"recipeID\")\n" +
                "\tVALUES (?,?) returning \"ID\";", new InstAndIngredIDMapper(), newInstruct2.getInstructionLine(), newInstruct2.getRecipeID());
        newInstruct2.setID(insertedIdsDir2.get(0));

    }


    @Override
    public Recipe getRecipeByID(Integer id) {
        Recipe retrievedRecipe = template.queryForObject("SELECT * from \"Recipes\" WHERE \"ID\" = "+id+";",
                new RecipeMapper());
        return retrievedRecipe;
    }



    @Override
    public Recipe addRecipe(Recipe recipe) {
        //      if(recipe == null){
        //            throw new NullRecipeException("Received null for recipe object.");
        //        }
//        List<Integer> insertedIds = template.query("INSERT INTO public.\"Recipes\"(\n" +
//                "\ttitle, description, category, \"foodType\", \"prepTime\", \"cookTime\", servings, \"publicRecipe\", \"userID\", calories, \"totalTime\")\n" +
//                "\tVALUES ('"+recipe.getTitle()+"', '"+recipe.getDescription()+"', '"+recipe.getCategory()+"', '"+recipe.getFoodType()+"', "+recipe.getPrepTime()+", "+recipe.getCookTime() +", "+recipe.getServings()+", "+recipe.isPublicRecipe()+", "+recipe.getUserID()+", "+recipe.getCalories()+", "+recipe.getTotalTime()+") returning \"ID\";", new RecipeIDMapper());

        List<Integer> insertedIds = template.query("INSERT INTO public.\"Recipes\"(\n" +
                "\ttitle, description, category, \"foodType\", \"prepTime\", \"cookTime\", servings, \"publicRecipe\", \"userID\", calories, \"totalTime\")\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning \"ID\";", new RecipeIDMapper(), recipe.getTitle(), recipe.getDescription(), recipe.getCategory(),recipe.getFoodType(), recipe.getPrepTime(), recipe.getCookTime(), recipe.getServings(), recipe.isPublicRecipe(), recipe.getUserID(), recipe.getCalories(), recipe.getTotalTime());
        recipe.setId(insertedIds.get(0));
        return recipe;
    }

    @Override
    public int getUserIDByUsername(String username) {
        int userID = template.queryForObject("SELECT \"userID\"\n" +
                        "\tFROM public.\"Users\"\n" +
                        "\tWHERE \"username\" = '"+username+"';",
                new UserIDMapper());
        throw new UnsupportedOperationException();
    }



    class UserIDMapper implements RowMapper<Integer>{
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("userID");
        }
    }

    class RecipeIDMapper implements RowMapper<Integer>{
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("ID");

        }
    }

    class InstAndIngredIDMapper implements RowMapper<Integer>{
        @Override
        public Integer mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getInt("ID");
        }
    }

    class InstructionMapper implements RowMapper<Instruction>{
        @Override
        public Instruction mapRow(ResultSet resultSet, int i) throws SQLException {
            Instruction toReturn = new Instruction();
            toReturn.setID(resultSet.getInt("ID"));
            toReturn.setInstructionLine(resultSet.getString("instructionLine"));
            toReturn.setRecipeID(resultSet.getInt("recipeID"));
            return toReturn;
        }
    }

    class IngredientMapper implements RowMapper<Ingredient>{
        @Override
        public Ingredient mapRow(ResultSet resultSet, int i) throws SQLException {
            Ingredient toReturn = new Ingredient();
            toReturn.setID(resultSet.getInt("ID"));
            toReturn.setIngredientStr(resultSet.getString("ingredientStr"));
            toReturn.setRecipeID(resultSet.getInt("recipeID"));
            return toReturn;
        }
    }

    class RecipeMapper implements RowMapper<Recipe>{

        @Override
        public Recipe mapRow(ResultSet resultSet, int i) throws SQLException {
            Recipe toReturn = new Recipe();
            toReturn.setId(resultSet.getInt("ID"));
            toReturn.setTitle(resultSet.getString("title"));
            toReturn.setDescription(resultSet.getString("description"));
            toReturn.setCategory(resultSet.getString("category"));
            toReturn.setFoodType(resultSet.getString("foodType"));
            toReturn.setPrepTime(resultSet.getInt("prepTime"));
            toReturn.setCookTime(resultSet.getInt("cookTime"));
            toReturn.setServings(resultSet.getDouble("servings"));
            toReturn.setPublicRecipe(resultSet.getBoolean("publicRecipe"));
            toReturn.setUserID(resultSet.getInt("userID"));
            toReturn.setCalories(resultSet.getInt("calories"));
            toReturn.setTotalTime(resultSet.getInt("totalTime"));
            return toReturn;
        }
    }
}
