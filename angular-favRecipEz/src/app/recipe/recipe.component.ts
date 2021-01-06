import { Component, OnInit, Input } from '@angular/core';
import {Recipe} from '../recipe';

import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {RecipeService} from '../recipe.service';
import { Instruction } from '../instruction';
import { Ingredient } from '../Ingredient';
import { TokenStorageService } from '../token-storage.service';
import { User } from '../user';


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
  userOwnsRecipe: boolean;

  constructor(
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private location:Location,
    private routr: Router,
    private tokenStorServ: TokenStorageService
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
     
      let user: User = this.tokenStorServ.getUserData();
      console.log("USER USER :" + user.id + " " + user.userName);
      if(user && user.id===recipe.userID){
        this.userOwnsRecipe=true;
      }
    })
  }

  getDirsForRecipe(): void{
    const id = +this.route.snapshot.paramMap.get('id');
    this.recipeService.getInstForRecipe(id).subscribe(instructions => {
      console.log("instructions!!");   
      console.log(instructions);  
      this.instructions = this.putInNumericalOrder(instructions, (inst1: Instruction, inst2: Instruction) => {
          if(inst1.id > inst2.id){
            return -1;
          }else if(inst1.id < inst2.id){
            return 1;
          }else{
            return 0;
          }
      });

    })
  }

  getIngredForRecipe():void{
    const id = +this.route.snapshot.paramMap.get('id');
    this.recipeService.getIngredForRecipe(id).subscribe(ingredients =>  {
      console.log(ingredients);
      this.ingredients = this.putInNumericalOrder(ingredients, (ingred1: Ingredient, ingred2: Ingredient)=>{
          console.log(typeof ingred1.id);
          if(ingred1.id > ingred2.id){
            return -1;
          }else if(ingred1.id < ingred2.id){
            return 1;
          }else{
            return 0;
          }
      })

    })
  }

  putInNumericalOrder(objects: Object[], callback:Function):any{
    let sorted = this.quickSort(objects, callback);
    console.log("SORTED: ");
    for(let i = 0 ; i < sorted.length; i++){
      console.log(sorted[i].id);
      console.log(sorted[i].instructionLine);
      console.log(sorted[i].ingredientStr);
    }
    return sorted;
    // return this.quickSort(objects, callback);
}

defaultCallback = function(el1:number, el2:number){
  if(el1 > el2){
    return -1;
  }else if (el2 > el1){
    return 1;
  }else{
    return 0;
  }
}

quickSort = function(array: Object[], callback: Function) {
    if(array.length < 2) return array;
    if(!callback)callback= this.defaultCallback;
    let first:Object = array[0];
    let leftArr:Object[]=[];
    let rightArr: Object[] = [];
    for(let i: number = 1; i < array.length; i++){
        if(callback(array[i], first )===1){
            leftArr.push(array[i]);
        }else{
          rightArr.push(array[i])
        }
    }
    return this.quickSort(leftArr, callback).concat(first).concat(this.quickSort(rightArr, callback));
}


  edit():void{
    this.routr.navigate([`/editRecipe`], {queryParams: {"recipeID": this.recipe.id}})
  }

  

  goBack(): void{
    this.location.back();
  }

  save(): void{
    this.recipeService.updateRecipe(this.recipe)
      .subscribe(()=> this.goBack());
      
  }
}
