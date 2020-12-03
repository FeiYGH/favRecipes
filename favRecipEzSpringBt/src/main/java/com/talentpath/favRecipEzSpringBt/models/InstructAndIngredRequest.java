package com.talentpath.favRecipEzSpringBt.models;

import javax.validation.constraints.NotBlank;

//view model to hold incoming data, so don't have to do @Entity and @Table
public class InstructAndIngredRequest {

    @NotBlank
    private  Integer recipeID;

    @NotBlank
    private String[] ingredOrInstructLines;

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String[] getIngredOrInstructLines() {
        return ingredOrInstructLines;
    }

    public void setIngredOrInstructLines(String[] ingredOrInstructLines) {
        this.ingredOrInstructLines = ingredOrInstructLines;
    }
}
