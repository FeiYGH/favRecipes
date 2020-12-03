import { Component, Directive, OnInit } from '@angular/core';
import { NgControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { RegistrationRequest } from '../registration-request';
import { TokenStorageService } from '../token-storage.service';

// import { untilDestroyed } from 'ngx-take-until-destroy';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})

//error handling
// @Directive({
//   selector: '[formControl], [formControlName]'
//  })
 

export class RegisterComponent implements OnInit {
  loggedIn: boolean = false;
  registData: RegistrationRequest = {
    username: "",
    email: "",
    password: ""
  }

  errorMsgs: String[] = ["","",""];
  errorMsgUsername:string ="";
  errorMsgEmail:string ="";
  errorMsgPassword:string ="";
  
  
  confirmPasswd: string;
  constructor(private tokenStorServ: TokenStorageService, private authServ: AuthService, private routr: Router) { }

  ngOnInit(): void {
    if(this.tokenStorServ.getToken())this.loggedIn = true;
  }

  onSubmit():void{
    this.errorMsgs = ["", "", ""];
    if(this.registerDataValid()){
      this.authServ.register(this.registData).subscribe(msgResp => {
        //todo: send over query parameter
        this.routr.navigate(["signin"], { queryParams: {successRegMsg: "Congrats! You have successfully registered! Log In!"}})
      },
      (err) => {
        console.log(err.error.message)
        this.errorMsgs[0] = err.error.message;
      });
    }
  }

  registerDataValid(){
    let valid:boolean = true;
    let idx:number  = 0;
    if(this.registData.username===""){
      this.errorMsgs[idx]+="* Username can not be blank."
      idx++;
      // this.errorMsgUsername+= "Username can not be blank.";
      valid = false;
    }else if(this.registData.username.length < 3 || this.registData.username.length > 20){
      this.errorMsgs[idx]+="* Username must be between 3 and 20 characters.\n\n";
      idx++;
      valid = false;
    }
    if(this.registData.email===""){
      this.errorMsgs[idx]+= "* Email can not be blank.\n\n";
      idx++;
      valid = false;
    }else if(this.registData.email.length > 50){
      this.errorMsgs[idx]+= "* Email must be less than 50 characters.\n";
      idx++;
      valid = false;
    }

    if(this.registData.password===""){
      this.errorMsgs[idx]+= "* Password can not be blank.\n";
      valid = false;
    }else if(this.registData.password.length < 6 || this.registData.password.length > 40){
      this.errorMsgs[idx]+="* Password must be between 6 and 40 characters.\n";
      valid = false;
    }
    if(this.registData.password !== this.confirmPasswd){
      this.errorMsgs[idx] += " Passwords must match.";
      valid = false;
    }
    return valid; 
  }
}
