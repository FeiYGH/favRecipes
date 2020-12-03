import { Component, OnInit } from '@angular/core';
import { Router, RouteReuseStrategy, ActivatedRoute } from '@angular/router';
import { AuthService } from '../auth.service';
import { LoginRequest } from '../login-request';
import { TokenStorageService } from '../token-storage.service';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {
  loggedIn: boolean = false;
  loginData: LoginRequest = {username: "", password: ""};
  registSuccessMsg: string = "";
  // loginErrorMsg: string ="";
  errorMsgs: String[] = ["", ""];

  constructor(private authServ: AuthService, private tokenStorServ: TokenStorageService, private routr: Router, private activatedRte: ActivatedRoute) { }
  
  ngOnInit(): void {
    this.activatedRte.queryParams
      .subscribe(params => {
        console.log(params);
        this.registSuccessMsg = params.successRegMsg;
      })
  }

  onSubmit():void{
      this.registSuccessMsg= "";
      this.errorMsgs = ["", ""];
      if(this.validLoginInfo()){
          this.authServ.login(this.loginData).subscribe(user => {
              this.tokenStorServ.saveUserData(user);
              this.tokenStorServ.saveToken(user.token);
              this.loggedIn = true;
              this.routr.navigate(["dashboard"]);
          },
          //getting back an HttpErrorResponse
          (err) => {
              console.log(err);
              this.errorMsgs[0] = "Incorrect username and/or password."
          }
        );
      }
      this.loginData.username = "";
      this.loginData.password = "";
  }

  validLoginInfo():boolean{
    let valid:boolean = true;
    let idx:number  = 0;
    if(this.loginData.username===""){
      this.errorMsgs[idx]+="Username can not be blank."
      idx++;
      valid = false;
    }
    if(this.loginData.password===""){
      this.errorMsgs[idx]+= "Password can not be blank.\n\n";
      idx++;
      valid = false;
    }
    return valid; 
  }
}
