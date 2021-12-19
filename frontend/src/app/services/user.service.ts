import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { loginInfo } from 'src/app/models/loginInfo';
import { NotificationService } from './notification.service';
import { tokens } from 'src/app/models/tokens';
import { Router } from '@angular/router';
import { JwtHelperService } from "@auth0/angular-jwt";
import { Observable, retry } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class UserService {

    private readonly loginUrl = 'http://localhost:8080/api/v1/login';
    private readonly refreshTokenUrl = 'http://localhost:8080/api/v1/user/refreshtoken';
    private accessToken = ""
    private jwtHelper = new JwtHelperService();

    constructor(private http: HttpClient,
                private notification: NotificationService,
                private router: Router) {}

    getAccessToken(): string {
        console.log("accessing the token");

        return this.accessToken;
    }

    getAccessToken2() : string
    {
        return localStorage.getItem("access_token")!!;
    }

    setAccessToken(token: string)
    {
        this.accessToken = token
        localStorage.setItem('access_token', token);
    }

    isAuthenticated(): boolean {
        const refresh_token = localStorage.getItem('refresh_token')!!;
        const refresh_token_expired = this.jwtHelper.isTokenExpired(refresh_token);
        // const access_token_expired = this.jwtHelper.isTokenExpired(this.accessToken);

        return !refresh_token_expired
    }

    login(user: loginInfo) {
        const form = new FormData;
        form.append('username', user.username);
        form.append('password', user.password);

        var request = this.http.post<tokens>(this.loginUrl, form, { observe: 'response' })

        request.subscribe({
            next: (value: HttpResponse<tokens>) => {
                this.setAccessToken(value.body!!.access_token);
                // localStorage.setItem('access_token', value.body!!.access_token);
                localStorage.setItem('refresh_token', value.body!!.refresh_token);
                this.changeRoute("/home");
            },
            error: err => { this.notification.showError(err); }
        });
    }

    refreshToken2() : Observable<HttpResponse<tokens>> {
        console.log("refreshing token2");

        const refresh_token = localStorage.getItem('refresh_token')!!;

        const headers = new HttpHeaders()
            .append(
                'Authorization',
                'Bearer ' + refresh_token
            );

        console.log("refreshing token3: " + refresh_token);

        const request = this.http.get<tokens>(this.refreshTokenUrl, { headers: headers, observe: 'response' })

        console.log("refreshing token4");

        return request;
    }

    clearTokens()
    {
        localStorage.removeItem("refresh_token");
        this.setAccessToken("");
    }

    private changeRoute(route: string) {
        this.router.navigate([route]);
    }
}
