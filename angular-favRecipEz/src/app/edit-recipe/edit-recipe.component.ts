import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, Params, Router } from '@angular/router';
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
  origNumInstLines: number;
  instructions: Instruction[];
  origNumIngredLines: number;
  ingredients: Ingredient[];
  publicRecipe: boolean;
  allInstructions:string ="";
  allIngredients:string ="";
  ingredientsToAdd:string = "";
  noChangedIngredients:boolean=false;
  noChangedInstructions:boolean=false;
  compositeView: boolean = true;
  individualView: boolean = false;
  newInstruction: boolean = false;
  newInstructionLine:string ="";



  constructor(
    private activatedRte: ActivatedRoute,
    private recipeService: RecipeService, 
    private routr: Router) 
    { }

  ngOnInit(): void {
    // this.activatedRte.queryParams
    //     .subscribe(params => {
    //       let recipeID: number = parseInt(params.recipeID);
    //       this.recipeService.getRecipe(recipeID)
    //         .subscribe(retrievedRecipe => this.recipeToEdit = retrievedRecipe);
    //         this.recipeService.getInstForRecipe(recipeID)
    //           .subscribe(instructions => this.instructions = instructions);
    //         this.recipeService.getIngredForRecipe(recipeID)
    //           .subscribe(ingreds => {
    //             this.ingredients = ingreds;
    //             console.log("INgredients in edit:" + ingreds);
    //           });
    //     });
    let recipeID:number;
    this.activatedRte.queryParams.subscribe(params => {
      recipeID = parseInt(params.recipeID);
    })
    this.getRecipe(recipeID);
    this.getIngredForRecipe(recipeID);
    this.getDirsForRecipe(recipeID);
  }

  getRecipe(recipeID: number): void{
    // const id = +this.activatedRte.snapshot.paramMap.get('id');
    this.recipeService.getRecipe(recipeID).subscribe(recipe=> {
      this.recipeToEdit=recipe;
      this.publicRecipe = recipe.publicRecipe;
      
    })
  }

  getDirsForRecipe(recipeID:number): void{
    // const id = +this.activatedRte.snapshot.paramMap.get('id');
    this.recipeService.getInstForRecipe(recipeID).subscribe(instructions => {
      // console.log("instructions!!");   
      // console.log(instructions[0].instructionLine);  
      this.instructions = instructions;
      this.origNumInstLines = instructions.length;
      this.compileInstructionsToString(this.instructions);
    })
  }

  compileInstructionsToString(instructions:Instruction[]): void{
    this.allInstructions = "";
    for(let instruction of instructions){
      this.allInstructions+=instruction.instructionLine.trim();
      this.allInstructions+="\n\n";
    }
  }

  getIngredForRecipe(recipeID:number):void{
    // const id = +this.activatedRte.snapshot.paramMap.get('id');
    this.recipeService.getIngredForRecipe(recipeID).subscribe(ingredients =>  {
      this.ingredients = ingredients;
      this.origNumIngredLines = ingredients.length;
      for(let ingredient of ingredients){
        this.allIngredients+=ingredient.ingredientStr.trim();
        this.allIngredients+='\n\n';
      }
    })
  }

  edit():void{
    let recipeID:number = this.recipeToEdit.id;
    this.recipeService.updateRecipe(this.recipeToEdit).subscribe(() => {
       console.log('Edited recipe but not ingredients or instructions at this point');
       
       if(this.noChangedIngredients===false){
          //checking if new add'l ingreds and if so, add them
          let newIngreds: string[] = this.parseLines(this.ingredientsToAdd);
          if(newIngreds.length>0)this.recipeService.addRecipeIngredients(recipeID, newIngreds).subscribe(()=>{
            console.log("added new ingredients in edit recipe");
          });

          //checking if any ingredients have been deleted
          let currIngreds: string[] = this.parseLines(this.allIngredients);
          if(currIngreds.length < this.origNumIngredLines){
            let ingredientsToDelete: Ingredient[] = this.ingredients.slice(currIngreds.length);
            this.recipeService.deleteRecipeIngredients(recipeID, ingredientsToDelete).subscribe(()=>{
              console.log("deleted ingredients");
            })
          }

          //checking if the old ingreds have changed
          let editedIngreds: Ingredient[] = this.checkIngredientChanges(this.allIngredients);
          if(editedIngreds.length>0){
              this.recipeService.updateRecipeIngredients(editedIngreds).subscribe(()=>{
                console.log("updated ingreds!");
              })
          }
       }

       if(this.noChangedInstructions===false){
          let newAndOldLines:string[]=this.parseLines(this.allInstructions);
          //checking if current instructions have been edited (only checks up to old number of lines)
          let editedInstructions: Instruction[] = this.checkInstructionChanges(newAndOldLines);
          //if there are edits, make edits
          if(editedInstructions.length > 0){
              this.recipeService.updateRecipeInstructions(editedInstructions).subscribe(()=>{
                console.log("updated instructions!");
              })
          }

          if(this.compositeView===true){
              //checking if new add'l instructions have been added. If so, add them
              if(newAndOldLines.length > this.origNumInstLines){
                let newInstructions:string[] = newAndOldLines.slice(this.origNumInstLines);
                this.recipeService.addRecipeInstructions(recipeID, newInstructions).subscribe((addedInstructs) => {
                  console.log("Added New Instructions From Edit Form:"+ addedInstructs);
                })
              }
          }else if(this.individualView===true){
            if(this.instructions.length > this.origNumInstLines){
              let newInstructions:Instruction[]= this.instructions.slice(this.origNumInstLines);
              if(newInstructions.length > 0){
                this.recipeService.addRecipeInstructionsFromEdit(newInstructions).subscribe(()=> console.log("addedRecipeInstructions from Edit Component"))
              }
            }
          } 
       }
       

       this.routr.navigate([`/recipes/${this.recipeToEdit.id}`])
    })
  }

  checkInstructionChanges(allInstructs:string[]){
    let editedInstructs: Instruction[] = [];
     for(let i:number = 0 ; i < this.origNumInstLines; i++){
        if(this.instructions[i].instructionLine!==allInstructs[i]){
          this.instructions[i].instructionLine = allInstructs[i];
          editedInstructs.push(this.instructions[i]);
        }
     }
     return editedInstructs;
  }

  checkIngredientChanges(allIngreds:string){
    let allIngredLines:string[] = this.parseLines(allIngreds);
    let editedIngreds: Ingredient[] = [];
    for(let i:number =0; i < this.origNumIngredLines;i++){
      if(this.ingredients[i].ingredientStr!==allIngredLines[i]){
          this.ingredients[i].ingredientStr=allIngredLines[i];
          editedIngreds.push(this.ingredients[i]);
      }
    }
    return editedIngreds;
  }

  parseLines(lines: string): string[]{
    let indLines: string[];
    indLines = lines.split('\n').filter(line => line.trim().length > 0);
    console.log("IN PARSE LINES:" + indLines);
    console.log(JSON.stringify(indLines));
    return indLines;
  }
     
  processEditedInstructions(instructions: string): Instruction[]{
    let linesArr: string[] = this.allInstructions.split('\n');
    let instructionsArr =[];
    linesArr.forEach(line => instructionsArr.push({"instructionLine": line, "recipeID": 1 }))
    return instructionsArr;
  }

  selectCompView(): void{
    this.compositeView = true;
    this.individualView  = false;
  }

  selectIndView(): void{
    this.compositeView = false;
    this.individualView  = true;
  }

  moveUp(idx:number):void{
    if(idx===0)return;
    let temp: Instruction = this.instructions[idx-1];
    this.instructions[idx -1 ] =this.instructions[idx];
    this.instructions[idx] = temp;
    this.compileInstructionsToString(this.instructions);

  }

  moveDown(idx: number):void{
    if(idx===this.instructions.length -1)return;
    let temp: Instruction = this.instructions[idx+ 1];
    this.instructions[idx + 1 ] =this.instructions[idx];
    this.instructions[idx] = temp;
    this.compileInstructionsToString(this.instructions);
  }


  addNewInstruction(): void{
     let newInstruction:Instruction = {
       id:0,
       instructionLine: this.newInstructionLine,
       recipeID: this.recipeToEdit.id
     }
     this.instructions.push(newInstruction);
     this.newInstructionLine="";
     this.compileInstructionsToString(this.instructions);
  }
}
