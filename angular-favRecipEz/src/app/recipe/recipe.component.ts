import { Component, OnInit, Input } from '@angular/core';
import {Recipe} from '../recipe';

import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {RecipeService} from '../recipe.service';
import { Instruction } from '../instruction';
import { Ingredient } from '../Ingredient';


@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.css']
})
export class RecipeComponent implements OnInit {
  // @Input()recipe: Recipe;
  recipe:Recipe;
  instructions: Instruction[];
  ingredients: Ingredient[];
  publicRecipe:boolean;

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private location:Location
  ) { }

  ngOnInit(): void {
    this.getRecipe();
    this.getIngredForRecipe();
    this.getDirsForRecipe();
    
  }

  getRecipe(): void{
    const id = +this.route.snapshot.paramMap.get('id');
    this.recipeService.getRecipe(id).subscribe(recipe=> {
      this.recipe=recipe;
      this.publicRecipe = recipe.publicRecipe;
    })
  }

  getDirsForRecipe(): void{
    const id = +this.route.snapshot.paramMap.get('id');
    this.recipeService.getInstForRecipe(id).subscribe(instructions => {
      console.log("instructions!!");   
      console.log(instructions);  
      this.instructions = instructions;
    })
  }

  getIngredForRecipe():void{
    const id = +this.route.snapshot.paramMap.get('id');
    this.recipeService.getIngredForRecipe(id).subscribe(ingredients =>  {
      console.log(ingredients);
      this.ingredients = ingredients;
    })
  }

  

  goBack(): void{
    this.location.back();
  }

  save(): void{
    this.recipeService.updateRecipe(this.recipe)
      .subscribe(()=> this.goBack());
      
  }
}
