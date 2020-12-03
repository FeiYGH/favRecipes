package com.talentpath.favRecipEzSpringBt.daos;

import com.talentpath.favRecipEzSpringBt.models.Ingredient;
import com.talentpath.favRecipEzSpringBt.models.Instruction;
import com.talentpath.favRecipEzSpringBt.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Instruction addInstruction(Instruction newInstruct) {
        //      if(line == null){
        //            throw new NullDirectionException("Received null for direction string.");
        //

        List<Integer> insertedIds = template.query("INSERT INTO public.\"Directions\"(\n" +
                "\t\"instructionLine\", \"recipeID\")\n" +
                "\tVALUES ('"+newInstruct.getInstructionLine()+"', "+newInstruct.getRecipeID()+") returning \"ID\";", new InstAndIngredIDMapper());
        newInstruct.setID(insertedIds.get(0));
        return newInstruct;
    }

    @Override
    public Ingredient addIngredient(Ingredient newIngred) {
        //      if(line == null){
        //            throw new NullIngredientException("Received null for ingredient string.");
        //
        List<Integer> insertedIds = template.query("INSERT INTO public.\"Ingredients\"(\n" +
                "\t\"ingredientStr\", \"recipeID\")\n" +
                "\tVALUES ('"+newIngred.getIngredientStr()+"', "+newIngred.getRecipeID()+") returning \"ID\";", new InstAndIngredIDMapper());
        newIngred.setID(insertedIds.get(0));
        return newIngred;
    }

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
        List<Integer> insertedIds = template.query("INSERT INTO public.\"Recipes\"(\n" +
                "\ttitle, description, category, \"foodType\", \"prepTime\", \"cookTime\", servings, \"publicRecipe\", \"userID\", calories, \"totalTime\")\n" +
                "\tVALUES ('"+recipe.getTitle()+"', '"+recipe.getDescription()+"', '"+recipe.getCategory()+"', '"+recipe.getFoodType()+"', "+recipe.getPrepTime()+", "+recipe.getCookTime() +", "+recipe.getServings()+", "+recipe.isPublicRecipe()+", "+recipe.getUserID()+", "+recipe.getCalories()+", "+recipe.getTotalTime()+") returning \"ID\";", new RecipeIDMapper());
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
