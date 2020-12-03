import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';

//needs to sit btw every outgoing request
@Injectable()
export class AuthInterceptor implements HttpInterceptor{
    constructor(private tokenStor: TokenStorageService){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let token: string = this.tokenStor.getToken();
        if(token){
            //clone w/some new data
            req = req.clone({
                headers: req.headers.set("Authorization", "Bearer " + token)
            });

        }
        //continue process with same req that we may or may not have altered
        return next.handle(req);
    } 

}