import { Component, OnInit, Input } from '@angular/core';
import { Instruction } from '../instruction';
import { Ingredient } from '../Ingredient';

import {Recipe} from '../recipe';
import {RecipeService} from '../recipe.service'
import { Router } from '@angular/router';


@Component({
  selector: 'app-add-recipe',
  templateUrl: './add-recipe.component.html',
  styleUrls: ['./add-recipe.component.css']
})
export class AddRecipeComponent implements OnInit {

  // recipe: Recipe ={
  //   id: 0,
  //   title: "",
  //   description: "",
  //   category: "",
  //   foodType: "",
  //   prepTime: 0,
  //   cookTime: 0,
  //   servings: 0,
  //   publicRecipe:false,
  //   userID: 0,
  //   calories: 0,
  //   totalTime: 0
  // }

  recipe: any = {
    // id:7,
    title: "",
    description: "",
    category: "",
    foodType: "",
    prepTime: 0,
    cookTime: 0,
    servings: 0,
    publicRecipe:false,
    // userID: 0,
    calories: 0,
    totalTime: 0
}

recipeID:number;

ingredients: string = "";
instructions: string = "";

  constructor(private recipeService: RecipeService, private routr:Router) { }

  ngOnInit(): void {
    //need to grab the userID
    //could have it as a hidden label on the corner of every page
    
  }

  add(): void{
      let recipeID:number;
      console.log(this.recipe);
      // this.recipe.userID = 1; 
      let instructionsArr: Instruction[] = this.processInstructions(this.instructions);
      let ingredientsArr: Ingredient[] = this.processIngredients(this.ingredients);
      //call a validate information method here

      if(this.recipe.category==="")this.recipe.category = "Beef";
      this.recipeService.addRecipe(this.recipe as Recipe)
        .subscribe(recipe => {
          console.log("Successfully added recipe.");
          console.log(recipe);
          this.recipeID= recipe.id;
          
          this.recipeService.addRecipeInstructions(recipe.id, this.parseLines(this.instructions)).subscribe(instructions => {
            console.log("Successfully added recipe instructions");
            console.log(instructions)
          });
          this.recipeService.addRecipeIngredients(recipe.id, this.parseLines(this.ingredients)).subscribe(ingredients => {
            console.log("Successfully added recipe ingredients");
            console.log(ingredients);
            this.routr.navigate([`recipes/${this.recipeID}`])
          });
          
        })  
  };

  parseLines(lines: string): string[]{
    let indLines: string[];
    indLines = lines.split('\n').filter(line => line.trim().length > 0).map(line => JSON.stringify(line));
    console.log(indLines);
    console.log(JSON.stringify(indLines));
    return indLines;
  }
     
  
  
  processInstructions(instructions: string): Instruction[]{
    let linesArr: string[] = this.instructions.split('\n');
    let instructionsArr = [];
    linesArr.forEach(line => instructionsArr.push({"instructionLine": line, "recipeID": 1 }));
    return instructionsArr;
  }

  processIngredients(ingredients: string): Ingredient[]{
    let linesArr: string[] = this.ingredients.split('\n');
    let ingredientsArr = [];
    linesArr.forEach(line => ingredientsArr.push({"ingredientStr": line, "recipeID": 1 }));
    return ingredientsArr;
  }
  



}
