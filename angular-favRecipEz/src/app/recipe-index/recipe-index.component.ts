import { Component, OnInit } from '@angular/core';
import {Recipe} from '../recipe';
import {RECIPES} from '../mock-recipes';
import { HttpErrorResponse } from '@angular/common/http';
import {RecipeService} from '../recipe.service';
import {MessageService} from '../message.service';

@Component({
  selector: 'app-recipe-index',
  templateUrl: './recipe-index.component.html',
  styleUrls: ['./recipe-index.component.css']
})

export class RecipeIndexComponent implements OnInit {
  // recipes = RECIPES;
  
  recipes: Recipe[];

  //not using
  recipe: Recipe = {
    id: 1,
    title: 'Spaghetti',
    description: "Classic Italian Fav",
    category: "Beef",
    foodType: "Italian",
    prepTime: 10,
    cookTime: 30,
    servings: 4,
    publicRecipe: true,
    userID: 0,
    calories: 230,
    totalTime:40
  }

  // constructor(private recipeService: RecipeService, private messageService: MessageService) { }
  constructor(private recipeService: RecipeService) { }


  ngOnInit(): void {
    this.getRecipes();
  }

  //not using anymore
  selectedRecipe: Recipe;
  //not using onSelect anymore, using routerLink
    // onSelect(recipe:Recipe): void{
    //   this.selectedRecipe = recipe;
    //   this.messageService.add(`RecipeIndex Component: Selected recipe id =${recipe.id}`)
    // }

  // getRecipes(): void{
  //   this.recipes = this.recipeService.getRecipes();
  // }
  getRecipes(): void{
    this.recipeService.getRecipes().subscribe(recipes => this.recipes = recipes);
  }

      
  



}
