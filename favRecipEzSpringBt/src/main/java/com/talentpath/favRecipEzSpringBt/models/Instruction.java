package com.talentpath.favRecipEzSpringBt.models;

public class Instruction {
    int ID;
    String instructionLine;
    int recipeID;

    public Instruction(){};

    public Instruction(Integer recipeID, String instructionLine){
        this.recipeID = recipeID;
        this.instructionLine =instructionLine;
    }
    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getInstructionLine() {
        return instructionLine;
    }

    public void setInstructionLine(String instructionLine) {
        this.instructionLine = instructionLine;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
