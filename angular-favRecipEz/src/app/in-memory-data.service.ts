import { Injectable } from '@angular/core';
import {InMemoryDbService} from 'angular-in-memory-web-api';
import {Recipe} from './recipe';

@Injectable({
  providedIn: 'root'
})
export class InMemoryDataService implements InMemoryDbService{
  createDb(){
    const recipes = [
        {
          id: 1,
          title: 'Spaghetti',
          description: "Classic Italian Fav",
          category: "Beef",
          foodType: "Italian",
          prepTime: 10,
          cookTime: 30,
          servings: 4,
          publicRecipe: true,
          userID: 1,
          calories: 230,
          totalTime:40
      },
      {
          id: 2,
          title: 'Baked Salmon',
          description: "Delicious and Easy",
          category: "Salmon",
          foodType: "American",
          prepTime: 10,
          cookTime: 20,
          servings: 4,
          publicRecipe: true,
          userID: 1,
          calories: 200,
          totalTime:30
      },
      {
          id: 3,
          title: 'Waffles',
          description: "Breakfast foods",
          category: "",
          foodType: "Italian",
          prepTime: 10,
          cookTime: 30,
          servings: 4,
          publicRecipe: true,
          userID: 1,
          calories: 230,
          totalTime:40
      }
    ];
    return {recipes};
  }
  constructor() { }
  genId(recipes: Recipe[]): number{
    return recipes.length > 0 ? Math.max(...recipes.map(recipe => recipe.id)) + 1 : 4;
  }
}
