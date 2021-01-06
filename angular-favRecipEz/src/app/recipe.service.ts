import { Injectable } from '@angular/core';
import {Recipe} from './recipe';
import {RECIPES} from './mock-recipes';
import {MessageService} from './message.service';


import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { Observable, of } from 'rxjs';

import{catchError, map, tap} from 'rxjs/operators';
import { TokenType } from '@angular/compiler';
import { TokenizeOptions } from '@angular/compiler/src/ml_parser/lexer';
import { Instruction } from './instruction';
import { Ingredient } from './Ingredient';
import { DirIngredRequest } from './dir-ingred-request';



@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  
  // private recipesUrl = 'api/recipes';
  private recipesUrl:string = 'http://localhost:8080/api/recipes';
  private recipesSearchUrl:string = 'http://localhost:8080/api/recipesSearch';
  private directionsUrl:string = 'http://localhost:8080/api/recipeInstructions';
  private ingredientsUrl: string = 'http://localhost:8080/api/recipeIngredients';
  

  // httpOptions = {
  //   headers: new HttpHeaders({'Content-Type': 'application/json'})
  // }

  constructor( 
    private http: HttpClient,
    private messageService: MessageService
  ) { }

  getRecipes():Observable<Recipe[]>{
   //console.log(RECIPES);
   this.messageService.add('RecipeService: fetched recipes');
  //  return of(RECIPES);
  return this.http.get<Recipe[]>(this.recipesUrl)
      .pipe(
        tap(_ => this.log('fetched recipes')),
        catchError(this.handleError<Recipe[]>('getRecipes',[]))
      );
  }

  getRecipesForUser(): Observable<Recipe[]>{
      
    return this.http.get<Recipe[]>(this.recipesUrl + "/user"  )
      .pipe(
        tap(_ => this.log('fetched user recipes')),
        catchError(this.handleError<Recipe[]>('getRecipesForUser', []))
      )
  }

  getRecipe(id:number):Observable<Recipe>{
    this.messageService.add(`RecipeService: fetched recipe id=${id}`);
    const url = `${this.recipesUrl}/${id}`;
    return this.http.get<Recipe>(url).pipe(
      tap(_ => this.log(`fetched recipe id = ${id}`)),
      catchError(this.handleError<Recipe>(`getRecipe id=${id}`))
    )
    // return of(RECIPES.find(recipe=>recipe.id===id));
  } 

  getInstForRecipe(recipeID:number):Observable<Instruction[]>{
    this.messageService.add(`RecipeService: fetched instructions for recipe with id=${recipeID}`);
    const url = `${this.directionsUrl}/${recipeID}`;
    return this.http.get<Instruction[]>(url).pipe(
      tap(_=>this.log(`fetched instructions for recipe with id=${recipeID}`)),
      catchError(this.handleError<Instruction[]>(`getInstForRecipe recipeID=${recipeID}`))
    )    
  }

  getIngredForRecipe(recipeID: number):Observable<Ingredient[]>{
    this.messageService.add(`RecipeService: fetched ingredients for recipe with id=${recipeID}`);
    const url = `${this.ingredientsUrl}/${recipeID}`;
    return this.http.get<Ingredient[]>(url).pipe(
      tap(_ => this.log(`fetched ingredients for recipe with id=${recipeID}`)),
      catchError(this.handleError<Ingredient[]>(`getIngredForRecipe with recipeID=${recipeID}`))
    )
  }

  private log(message: string){
    this.messageService.add(`RecipeService: ${message}`);
  }

  //handle the http operation that failed and let the app continue
  //operation - name of the operation that failed
  //result - optional value to return as the observable result

  private handleError<T>(operation = 'operation', result?:T ){
    return(error: any): Observable<T> => {
      console.log(error);
      this.log(`${operation} failed ${error.message}`);
      return of(result as T);
    };
  }

  httpOptions = {
    headers: new HttpHeaders({'Content-Type':'application/json'})
  }

  updateRecipe(recipe:Recipe): Observable<any>{
    const url = `${this.recipesUrl}/${recipe.id}`;
    return this.http.put(url, recipe, this.httpOptions).pipe(
      tap(_=> this.log(`updated recipe id =${recipe.id}`)),
      catchError(this.handleError<any>('updateRecipe'))
    )
  }

  updateRecipeIngredients(editedIngreds: Ingredient[]):Observable<any>{
    return this.http.put<void>(this.ingredientsUrl, editedIngreds , this.httpOptions).pipe(
      tap(() => this.log(`updated ingredients for recpe with id=${editedIngreds[0].id}`)),
      catchError(this.handleError<void>('updatedRecipeIngredients'))
    )
  }

  updateRecipeInstructions(editedInstructs: Instruction[]):Observable<any>{
    return this.http.put<void>(this.directionsUrl, editedInstructs, this.httpOptions).pipe(
      tap(()=> this.log(`updated instructions for recipe with id=${editedInstructs[0].recipeID}`)),
      catchError(this.handleError<void>('updatedRecipeInstructions'))
    )
  }

  addRecipe(recipe: Recipe): Observable<Recipe>{
      return this.http.post<Recipe>(this.recipesUrl, recipe, this.httpOptions).pipe(
         tap((newRecipe: Recipe) => this.log(`added recipe with id=${newRecipe.id}`)),
         catchError(this.handleError<Recipe>('addRecipe'))
      )
  }

  //recipId is the recipeID 
  addRecipeInstructions(recipId: number, instructionsArr : string[]):Observable<any> {
    //the response expects back an Instruction array
    let test1 = recipId;
    let test2= instructionsArr;
    let dirIngredReq: DirIngredRequest = {recipeID: recipId, ingredOrInstructLines: instructionsArr};
    // dirIngredReq.recipeID = ID;
    // dirIngredReq.ingredOrInstructLines = instructionsArr;
    return this.http.post<Instruction[]>(this.directionsUrl, dirIngredReq, this.httpOptions).pipe(
      tap((newDirections: Instruction[]) => this.log(`added directions to recipe with id=${recipId}`)),
      catchError(this.handleError<Instruction[]>('addRecipeDirections'))
    ) 
  }

  deleteRecipeIngredients(recipeId: number, ingredientsToDelete: Ingredient[]):Observable<number>{
    let url:string;
    url = this.ingredientsUrl + "/" + recipeId;
      return this.http.delete<number>(url, this.httpOptions).pipe(
          tap(() => this.log(`deleted ingredients with recipeID=${recipeId}`)),
          catchError(this.handleError<number>('deleteRecipeIngredients'))
      )
  }

  addRecipeInstructionsFromEdit(newInstructions:Instruction[]):Observable<any>{
    return this.http.post<Instruction[]>(this.directionsUrl + 'FromEdit', newInstructions, this.httpOptions).pipe(
      tap((newDirs: Instruction[])=> this.log(`added directions in editRecipe with id=${newInstructions[0].recipeID}`)),
      catchError(this.handleError<Instruction[]>('addedRecipeInstructionsObj from EditRecipe Component'))
    )
  }

  
  //recipId is recipeID
  addRecipeIngredients(recipId: number, ingredientsArr: string[]) {
    //the response expects back an Ingredients array
    let test2= ingredientsArr;
    return this.http.post<Ingredient[]>(this.ingredientsUrl, {"recipeID": recipId, "ingredOrInstructLines": ingredientsArr}, this.httpOptions).pipe(
      tap((newIngredients: Ingredient[]) => {
        this.log(`added ingredients to recipe with id=${recipId}`);
        console.log(newIngredients);
      }),
      
      catchError(this.handleError<Instruction[]>('addRecipeIngredients'))
    ) 
  }

  deleteDirsForRecipe(recipe:Recipe | number):  Observable<number>{
    const id = typeof recipe === 'number' ? recipe : recipe.id;
    const url = `${this.directionsUrl}/${id}`;
    console.log("In deleteDirsForRecipe in recipe.service");
      return this.http.delete<number>(url, this.httpOptions).pipe(
        tap( _ => this.log(`deleted directions for recipe with id=${id}`)),
        catchError(this.handleError<number>('deleteDirsForRecipe'))
      );
  }

  deleteIngredsForRecipe(recipe:Recipe | number):  Observable<number>{
    const id = typeof recipe === 'number' ? recipe : recipe.id;
    const url = `${this.ingredientsUrl}/${id}`;
    console.log("In deleteIngredsForRecipe in recipe.service");
      return this.http.delete<number>(url, this.httpOptions).pipe(
        tap( _ => this.log(`deleted ingredients for recipe with id=${id}`)),
        catchError(this.handleError<number>('deleteIngredsForRecipe'))
      );
  }



  deleteRecipe(recipe:Recipe | number):  Observable<Recipe>{
      const id = typeof recipe === 'number' ? recipe : recipe.id;
      const url = `${this.recipesUrl}/${id}`;
      console.log("In deleteRecipe in recipe.service");
      return this.http.delete<Recipe>(url, this.httpOptions).pipe(
        tap( _ => this.log(`deleted recipe id=${id}`)),
        catchError(this.handleError<Recipe>('deleteRecipe'))
      );
  }


  searchRecipes(term:string, allOrInd: string): Observable<Recipe[]>{
    if(!term.trim()){
      //if not search term, return empty hero array.
      return of([]);
    }
    return this.http.get<Recipe[]>(`${this.recipesSearchUrl}/?title=${term}&allOrInd=${allOrInd}`).pipe(
      tap(x => x.length ? 
        this.log(`found recipes matching "${term}"`) : 
        this.log(`no recipes matching "${term}"`)),
      catchError(this.handleError<Recipe[]>('searchRecipes', [])) 
    );
  }

 
  deleteIngredient(ingredient: Ingredient | number): Observable<number>{
    const id = typeof ingredient === 'number' ? ingredient : ingredient.id;
    const url = `${this.ingredientsUrl}/ind/${id}`;
    return this.http.delete<number>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted recipe ingredient id=${id}`)),
      catchError(this.handleError<number>('deleted single Ingredient'))
    )
    
    
  }

  deleteInstruction(instruction:Instruction | number): Observable<Instruction>{
    const id = typeof instruction === 'number' ? instruction : instruction.id;
    const url = `${this.directionsUrl}/ind/${id}`;
    return this.http.delete<Instruction>(url, this.httpOptions).pipe(
      tap( _ => this.log(`deleted recipe instruction id=${id}`)),
      catchError(this.handleError<Instruction>('deleted single Instruction'))
    )
  }


}
