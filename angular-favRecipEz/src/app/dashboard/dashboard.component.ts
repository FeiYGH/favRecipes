import { Component, OnInit } from '@angular/core';
import { Data, NavigationExtras, Router } from '@angular/router';
import {Recipe} from '../recipe';
import {RecipeService} from '../recipe.service';
import { TokenStorageService } from '../token-storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  recipes: Recipe[] = [];
  noRecipesMsg: string="";

  constructor(private recipeService: RecipeService, private routr: Router, private tokenStorServ:TokenStorageService) { }

  ngOnInit(): void {
    this.getRecipes();
  }

  getRecipes():void{
    //in future, will change this to recipes that match id of logged in user
    this.recipeService.getRecipesForUser().subscribe(recipes => {
      this.recipes = recipes;
      if(this.recipes.length===0)this.noRecipesMsg = "You have no favorite recipes.";
    });
  }

  delete(recipe:Recipe): void{
      this.recipes = this.recipes.filter(r => r!==recipe);
      //this component will not anything w/the Observable returned by the 
      //recipeService.delete but must subscribe or will not send delete
      //request to the server 
      this.recipeService.deleteDirsForRecipe(recipe.id).subscribe((numRowsDeleted)=> {
        console.log("Successfully deleted directions for recipe");
        console.log("Num directions deleted: "+numRowsDeleted );
        this.recipeService.deleteIngredsForRecipe(recipe.id).subscribe((numRowsDel)=>{
          console.log("Successfully deleted ingredients for recipe");
          console.log("Num ingredients deleted: " + numRowsDel );
          this.recipeService.deleteRecipe(recipe.id).subscribe((numRowsD)=> {
            console.log("Successfully deleted recipe with ID=" + recipe.id);
            console.log("Num recipes deleted: "+ numRowsD);
          });
      
        });
    })
  }

  edit(recipe:Recipe):void{
    this.routr.navigate(["/editRecipe"], {queryParams: {"recipeID": recipe.id}});
  }

  loggedIn():boolean{
      if(this.tokenStorServ.getToken())return true;
      return false;
  }

  addRecipe():void{
    this.routr.navigate(["/addRecipe"]);
  }

}
