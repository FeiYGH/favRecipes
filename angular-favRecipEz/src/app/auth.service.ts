import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from './login-request';
import { MessageResponse } from './message-response';
import { RegistrationRequest } from './registration-request';
import { User } from './user';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(
    //client: consumer of service
    private clientHttp: HttpClient 
  ) { }

  
  login(loginCreds: LoginRequest): Observable<User>{
      return this.clientHttp.post<User>( //type indicator tells should be Observable of User object
        "http://localhost:8080/api/auth/signin", 
        loginCreds, 
        {
          headers: new HttpHeaders({"Content-Type": "application/json"}) //options
        }  
      );
      
  }

  register(registrationData: RegistrationRequest): Observable<MessageResponse>{
    return this.clientHttp.post<MessageResponse>(
      "http://localhost:8080/api/auth/register",
      registrationData,
      {
        headers: new HttpHeaders({"Content-Type": "application/json"}) //tells data come over in json format
      }
    );
  }
}


