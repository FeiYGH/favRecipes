import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, Params, Router } from '@angular/router';
// import { ConsoleReporter } from 'jasmine';
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
  originalInstructions: Instruction[] = [];
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


  editIngredView: boolean = true;
  deleteIngredView: boolean = false;
  deleteIndInstructsView: boolean = false;


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
      this.instructions = this.putInNumericalOrder(instructions, (inst1: Instruction, inst2: Instruction) => {
        if(inst1.id > inst2.id){
          return -1;
        }else if(inst1.id < inst2.id){
          return 1;
        }else{
          return 0;
        }
      });

      this.originalInstructions = [];
      for(let i:number =0; i < this.instructions.length ;i++){
         let instruction:Instruction = {
           id:0,
           instructionLine: "",
           recipeID: 0
         }

         instruction.id = this.instructions[i].id;
         instruction.instructionLine = this.instructions[i].instructionLine;
         instruction.recipeID = this.instructions[i].recipeID;
         this.originalInstructions.push(instruction);
      }

      this.origNumInstLines = instructions.length;
      console.log("INSTRUCTIONS AFTER BEEING ORDER")
      console.log(this.instructions);
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
      this.ingredients = this.putInNumericalOrder(ingredients, (ingred1: Ingredient, ingred2: Ingredient)=>{
          console.log(typeof ingred1.id);
          if(ingred1.id > ingred2.id){
            return -1;
          }else if(ingred1.id < ingred2.id){
            return 1;
          }else{
            return 0;
          }
      });
      this.origNumIngredLines = ingredients.length;
      for(let ingredient of this.ingredients){
        this.allIngredients+=ingredient.ingredientStr.trim();
        this.allIngredients+='\n\n';
      }
    })
  }

  putInNumericalOrder(objects: Object[], callback:Function):any{
      let sorted = this.quickSort(objects, callback);
      console.log("SORTED IN EDIT: ");
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
          let deletedIngredients:boolean;
          if(currIngreds.length < this.origNumIngredLines){
              deletedIngredients = true;
          }

          //checking if the old ingreds have changed
          let editedIngreds: Ingredient[] = this.checkIngredientChanges(this.allIngredients);
          if(editedIngreds.length>0){
               this.recipeService.updateRecipeIngredients(editedIngreds).subscribe(()=>{
                   console.log("updated ingreds!");
                   //if there have been deleted ingredients
                  //  if(deletedIngredients ===true){
                  //     currIngreds = this.parseLines(this.allIngredients);
                  //     let ingredientsToDelete: Ingredient[] = this.ingredients.slice(this.origNumIngredLines);
                  //     this.recipeService.deleteRecipeIngredients(recipeID, ingredientsToDelete).subscribe(()=>{
                  //       console.log("deleted ingredients");
                  //     })
                      
                  //  }
               })
               
          }
          
        }

      //*********** as long as user did not check box of no changed instructions*****/
       if(this.noChangedInstructions===false){
          if(this.compositeView===true){
            let newAndOldLines:string[]=this.parseLines(this.allInstructions);
            //checking if current instructions have been edited (only checks up to old number of lines)
            let editedInstructions: Instruction[] = this.checkInstructionChanges(newAndOldLines);
            //if there are edits, make edits
            if(editedInstructions.length > 0){
                this.recipeService.updateRecipeInstructions(editedInstructions).subscribe(()=>{
                    console.log("updated instructions composite view!");
                })
            }
          }else if(this.individualView===true){
            //checking if current instructions have been edited (only checks up to old number of lines)
            let editedInstructions:Instruction[] = this.checkInstructionChangesIndView(); 
            if(editedInstructions.length > 0){
              this.recipeService.updateRecipeInstructions(editedInstructions).subscribe(() => {
                  console.log("updated instructions individual view");
              })
            }
          }

          //processing any instructions that the user has added 
          let newAndOldLines:string[]=this.parseLines(this.allInstructions);
          if(this.compositeView===true){
              //checking if new add'l instructions have been added. If so, add them
              if(newAndOldLines.length > this.origNumInstLines){
                let newInstructions:string[] = newAndOldLines.slice(this.origNumInstLines);
                this.recipeService.addRecipeInstructions(recipeID, newInstructions).subscribe((addedInstructs) => {
                  console.log("Added New Instructions From Edit Form:" + addedInstructs);
                })
              }
          }else if(this.individualView===true){
            if(this.instructions.length > this.origNumInstLines){
              let newInstructions:Instruction[] = this.instructions.slice(this.origNumInstLines);
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
        if(this.originalInstructions[i].instructionLine!==allInstructs[i]){
          this.originalInstructions[i].instructionLine = allInstructs[i];
          editedInstructs.push(this.originalInstructions[i]);
        }
     }
     return editedInstructs;
  }

  checkInstructionChangesIndView(){
    let editedInstructs: Instruction[] = [];
    for(let i:number = 0 ; i < this.origNumInstLines; i++){
      if(this.originalInstructions[i].instructionLine!==this.instructions[i].instructionLine){
        this.originalInstructions[i].instructionLine = this.instructions[i].instructionLine;
        editedInstructs.push(this.originalInstructions[i]);
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

  selectEditIngredView(): void{
    this.editIngredView=true;
    this.deleteIngredView = false;
  }

  selectDeleteIngredView(): void{
    this.editIngredView=false;
    this.deleteIngredView = true;
    
  }



  selectCompView(): void{
    this.compositeView = true;
    this.individualView  = false;
    this.deleteIndInstructsView = false;
  }

  selectIndView(): void{
    this.compositeView = false;
    this.individualView  = true;
    this.deleteIndInstructsView = false;

  }

  selectDelInstructsView(): void{
    this.compositeView = false;
    this.individualView = false;
    this.deleteIndInstructsView = true;
  }

  moveUp(idx:number):void{
    if(idx===0)return;
    let temp: string = this.instructions[idx-1].instructionLine;
    this.instructions[idx - 1].instructionLine =this.instructions[idx].instructionLine;
    this.instructions[idx].instructionLine = temp;
    this.compileInstructionsToString(this.instructions);

  }

  moveDown(idx: number):void{
    if(idx===this.instructions.length -1)return;
    let temp: string = this.instructions[idx+ 1].instructionLine;
    this.instructions[idx + 1].instructionLine = this.instructions[idx].instructionLine;
    this.instructions[idx].instructionLine = temp;
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

  deleteIngredient(idx:number):void{
    let ingredientID = this.ingredients[idx].id;
    let recipeID = this.ingredients[idx].recipeID;
    this.recipeService.deleteIngredient(ingredientID).subscribe(()=> {
        console.log("successfully deleted ingredient with ID="+ingredientID);
        this.getRecipe(recipeID);
        this.getIngredForRecipe(recipeID);
        this.getDirsForRecipe(recipeID);

    })
  }

  deleteInstruction(idx:number):void{
    let instructionID = this.instructions[idx].id;
    let recipeID = this.instructions[idx].recipeID;
    this.recipeService.deleteInstruction(instructionID).subscribe(() => {
        console.log("successfully deleted instruction with ID=" + instructionID);
        this.getRecipe(recipeID);
        this.getIngredForRecipe(recipeID);
        this.getDirsForRecipe(recipeID);

    })
  }
}

