import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from './token-storage.service';
import { User } from './user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title:string = 'favRecip';
  title2: string='Ez';
  welcomeUser:string = 'test';

  constructor(private tokenStorServ: TokenStorageService, private routr:Router){}

  isLoggedIn():boolean{
      let token: string = this.tokenStorServ.getToken();
      let user: User = this.tokenStorServ.getUserData();
      if(user)this.welcomeUser = user.userName;
      if(token)return true;
      return false;
  }

  // setUsername():boolean{
  //   if(this.isLoggedIn){
  //     let user: User = this.tokenStorServ.getUserData();
  //     if(user)this.welcomeUser = user.userName;
  //     return true;
  //   }
  //   return false;
    
  // }

  logout():void{
    this.tokenStorServ.signout();
    this.routr.navigate(['/recipes']);
    //reloads the window, reloads the current url;
    // window.location.reload();

    
  }

}
