import { Component, OnInit } from '@angular/core';
import {Observable, Subject} from 'rxjs';

import{
  debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import {Recipe} from '../recipe';
import {RecipeService} from '../recipe.service';


@Component({
  selector: 'app-recipe-search',
  templateUrl: './recipe-search.component.html',
  styleUrls: ['./recipe-search.component.css']
})
export class RecipeSearchComponent implements OnInit {
  recipes$: Observable<Recipe[]>;
  //searchTerms is an RxJS Subject
  //both a source of observable values and an Observable itself
  //can subscribe to a Subject as you would to an Observable
  private searchTerms = new Subject<string>();

  constructor(private recipeService: RecipeService) { }

  //push a search term into that Observable (in observable stream) 
  //every time user types in textbox, binding calls search(), with textbox value,
  //a "search term"
  //searchTerms becomes an Observable emitting a steady stream of search terms
  search(term:string): void{
    this.searchTerms.next(term);
    console.log(this.recipes$);
    console.log("hi");
  }

  ngOnInit(): void {
    this.recipes$ = this.searchTerms.pipe(
      //wait 300ms after each keystroke bf considering the term
      debounceTime(300),
      //ignore new term if same as previou term
      distinctUntilChanged(),
      //switch to new search observable each time the term changes
      switchMap((term:string) => this.recipeService.searchRecipes(term, "all")),
      
      //more on switchMap
      //switchMap() preserves the original request order
      //returns only the observable from the most recent HTTP method call. 
      // Results from prior calls are canceled and discarded.
      // Note that canceling a previous searchHeroes() Observable doesn't actually abort a pending HTTP request. 
      // Unwanted results are simply discarded before they reach your application code
    
      )
  }

}
