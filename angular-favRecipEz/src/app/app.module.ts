import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';  

import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';

import { AppComponent } from './app.component';
import { RecipeIndexComponent } from './recipe-index/recipe-index.component';
import { RecipeComponent } from './recipe/recipe.component';
import { MessagesComponent } from './messages/messages.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';

//In-memory Web API imports
import {HttpClientInMemoryWebApiModule} from 'angular-in-memory-web-api';
import {InMemoryDataService} from './in-memory-data.service';
import { AddRecipeComponent } from './add-recipe/add-recipe.component';
import { RecipeSearchComponent } from './recipe-search/recipe-search.component';
import { SigninComponent } from './signin/signin.component';
import { RegisterComponent } from './register/register.component';
import { AuthInterceptor } from './auth-interceptor';
import { EditRecipeComponent } from './edit-recipe/edit-recipe.component';
import { ProfileComponent } from './profile/profile.component';


@NgModule({
  declarations: [
    AppComponent,
    RecipeIndexComponent,
    RecipeComponent,
    MessagesComponent,
    DashboardComponent,
    AddRecipeComponent,
    RecipeSearchComponent,
    SigninComponent,
    RegisterComponent,
    EditRecipeComponent,
    ProfileComponent
  ],
  imports: [
    BrowserModule,
    CommonModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,

    //remove HttpClientInMemoryWebApiModule when real server ready to receive requests
    //this intercepts HTTP requests and returns simulated server responses
    // HttpClientInMemoryWebApiModule.forRoot(InMemoryDataService, {dataEncapsulation: false})
  ],
  providers: [
    //not need to place any providers due to the 'providedIn' flag...
    {
      provide: HTTP_INTERCEPTORS, 
      useClass: AuthInterceptor,
      multi:true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
