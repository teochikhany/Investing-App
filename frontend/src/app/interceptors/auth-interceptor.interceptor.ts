import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, of, throwError } from 'rxjs';
import { UserService } from '../services/user.service';

@Injectable()
export class AuthInterceptorInterceptor implements HttpInterceptor {

    constructor() { }

    private handleAuthError(err: HttpErrorResponse): Observable<any> {
        //handle your auth error or rethrow
        if (err.status === 401 || err.status === 403) {
            console.log(err.error); // this is the one i want
            UserService.refreshToken();
            //navigate /delete cookies or whatever
            // if you've caught / handled the error, you don't want to rethrow it unless you also want downstream consumers to have to handle it as well.
            return of(err.message); // or EMPTY may be appropriate here
        }
        return throwError(err);
    }

    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>>
    {
        return next.handle(request).pipe(catchError(x=> this.handleAuthError(x)));
    }
}
