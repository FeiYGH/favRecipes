import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {RouterModule, Routes} from '@angular/router';
import {RecipeIndexComponent} from './recipe-index/recipe-index.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {RecipeComponent} from './recipe/recipe.component'; 
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { SigninComponent } from './signin/signin.component';
import { RegisterComponent } from './register/register.component';
import { EditRecipeComponent } from './edit-recipe/edit-recipe.component';
import {ProfileComponent} from './profile/profile.component';

const routes: Routes = [
  {path: '', redirectTo: '/recipes', pathMatch: 'full'},
  {path:'recipes', component:RecipeIndexComponent},
  {path:'dashboard', component: DashboardComponent},
  {path:'recipes/:id', component: RecipeComponent},
  {path:'addRecipe', component:AddRecipeComponent},
  {path: "signin", component: SigninComponent},
  {path: "register", component: RegisterComponent},
  {path:'editRecipe', component: EditRecipeComponent},
  {path: 'profile', component: ProfileComponent}
  
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})

export class AppRoutingModule { }
