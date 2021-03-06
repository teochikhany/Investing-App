import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { catchError, Observable, of, retry, switchMap, tap, throwError } from 'rxjs';
import { UserService } from '../services/user.service';
import { tokens } from '../models/tokens';

@Injectable()
export class AuthInterceptorInterceptor implements HttpInterceptor {

    constructor(private userService: UserService) { }

    // private handleAuthError(err: HttpErrorResponse, request: HttpRequest<unknown>, next: HttpHandler): Observable<any> {
    //     //handle your auth error or rethrow
    //     if (err.status === 401 || err.status === 403) {
    //         console.log(err.error); // this is the one i want
    //         return next.handle(request);
    //     }
    //     return throwError(err);
    // }

    // intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>>
    // {
    //     const test = request.clone();

    //     return next.handle(request).pipe( catchError(err => this.handleAuthError(err, request, next)) );
    // }


    intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
        return next.handle(request).pipe(catchError(error => {
            if (error.status === 403) {
                console.log("interceptor " + JSON.stringify(error.error));
                console.log("interceptor " + JSON.stringify(request));
                return this.userService.refreshToken2().pipe(
                    tap(() => console.log("here")),
                    switchMap((value) => {

                        console.log("value: " + JSON.stringify(value));
                        this.userService.setAccessToken(value.body!!.access_token);

                        const request2 = request.clone({
                            setHeaders: {
                                "Authorization": "Bearer " + this.userService.getAccessToken()
                            }
                        });

                        return next.handle(request2);
                    }),
                    catchError(e => {
                        console.log("eror: " + e)
                        return next.handle(request);
                    })
                );
                // return next.handle(request) // this works
            }
            return throwError(error);
          })
        )
      }
}
