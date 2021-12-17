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
    private static readonly refreshTokenUrl = 'http://localhost:8080/api/v1/user/refreshtoken';
    static accessToken = ""
    private static jwtHelper = new JwtHelperService();
    private static router: Router;
    private static http: HttpClient;
    private static notification: NotificationService;

    constructor(private http: HttpClient,
        private notification: NotificationService,
        private router: Router) {
            UserService.router = this.router;
        UserService.http = this.http;
        UserService.notification = this.notification;
    }

    static getAccessToken(): string {
        console.log("accessing the token");

        return this.accessToken;
    }

    static isAuthenticated(): boolean {
        const refresh_token = localStorage.getItem('refresh_token')!!;

        const access_token_expired = this.jwtHelper.isTokenExpired(this.accessToken);
        const refresh_token_expired = this.jwtHelper.isTokenExpired(refresh_token);

        return (!access_token_expired || !refresh_token_expired) && this.accessToken != ""
    }

    login(user: loginInfo) {
        const form = new FormData;
        form.append('username', user.username);
        form.append('password', user.password);

        var request = this.http.post<tokens>(this.loginUrl, form, { observe: 'response' })

        request.subscribe({
            next: (value: HttpResponse<tokens>) => {
                UserService.accessToken = value.body!!.access_token;
                localStorage.setItem('refresh_token', value.body!!.refresh_token);
                UserService.changeRoute("/home");
            },
            error: err => { this.notification.showError(err); }
        });
    }

    static refreshToken() {
        console.log("refreshing token");

        const refresh_token = localStorage.getItem('refresh_token')!!;

        const headers = new HttpHeaders()
            .append(
                'Authorization',
                'Bearer ' + refresh_token
            );

        var request = this.http.get<tokens>(this.refreshTokenUrl, { headers: headers, observe: 'response' })

        request.subscribe({
            next: (value: HttpResponse<tokens>) => { UserService.accessToken = value.body!!.access_token; },
            error: err => { this.notification.showError(err); }
        });
    }

    private static changeRoute(route: string) {
        UserService.router.navigate([route]);
    }
}
