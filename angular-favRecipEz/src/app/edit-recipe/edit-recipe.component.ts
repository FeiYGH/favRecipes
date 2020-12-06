import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, Params } from '@angular/router';
import { Ingredient } from '../Ingredient';
import { Instruction } from '../instruction';
import { Recipe } from '../recipe';
import { RecipeService } from '../recipe.service';

@Component({
  selector: 'app-edit-recipe',
  templateUrl: './edit-recipe.component.html',
  styleUrls: ['./edit-recipe.component.css']
})
export class EditRecipeComponent implements OnInit {
  recipeToEdit: Recipe 
  instructions:Instruction[];
  ingredients: Ingredient[];

  constructor(private activatedRte: ActivatedRoute, private recipeService: RecipeService) { }

  ngOnInit(): void {
    this.activatedRte.queryParams
        .subscribe(params => {
          this.recipeService.getRecipe(params.recipeID)
            .subscribe(retrievedRecipe => this.recipeToEdit = retrievedRecipe);
            this.recipeService.getInstForRecipe(params.recipeID)
              .subscribe(instructions => this.instructions = instructions);
            this.recipeService.getIngredForRecipe(params.recipeID)
              .subscribe(ingreds => this.ingredients = ingreds);
        });
  }

  edit():void{
    this.recipeService.updateRecipe(this.recipeToEdit).subscribe(recipe => {
       console.log('Edited recipe but not ingredients or instructions yet')
    })
  }

}
