// import { Injectable } from '@angular/core';
// import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse, HttpResponse } from '@angular/common/http';
// import { catchError, Observable, of, retry, switchMap, tap, throwError } from 'rxjs';
// import { UserService } from '../services/user.service';
// import { tokens } from '../models/tokens';

// @Injectable()
// export class AuthInterceptorInterceptor implements HttpInterceptor {

//     constructor() { }

//     private handleAuthError(err: HttpErrorResponse, request: HttpRequest<unknown>, next: HttpHandler): Observable<any> {
//         //handle your auth error or rethrow
//         if (err.status === 401 || err.status === 403) {
//             console.log(err.error); // this is the one i want
//             const refresh_request = UserService.refreshTokenObs()
//             refresh_request.subscribe({
//                 next: (value: HttpResponse<tokens>) => { UserService.accessToken = value.body!!.access_token; next.handle(request); },
//                 error: err => { console.log("errrroor") }
//             });
//             return refresh_request;
//             //navigate /delete cookies or whatever
//             // if you've caught / handled the error, you don't want to rethrow it unless you also want downstream consumers to have to handle it as well.
//             // return next.handle(request);
//             // return of(err.message); // or EMPTY may be appropriate here
//         }
//         return throwError(err);
//     }

//     intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>>
//     {
//         const test = request.clone();

//         return next.handle(request).pipe( catchError(err => this.handleAuthError(err, request, next)) );
//     }
// }
