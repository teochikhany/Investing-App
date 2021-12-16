import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { loginInfo } from 'src/models/loginInfo';
import { NotificationService } from './notification.service';
import { tokens } from 'src/models/tokens';
import { Router } from '@angular/router';


@Injectable({
    providedIn: 'root'
})
export class UserService {

    private readonly loginUrl = 'http://localhost:8080/api/v1/login';
    private static accessToken = ""

    constructor(private http: HttpClient, private notification: NotificationService, private router: Router) { }

    static getAccessToken(): string {
        return this.accessToken;
    }

    login(user: loginInfo) {
        const form = new FormData;
        form.append('username', user.username);
        form.append('password', user.password);

        var request = this.http.post<tokens>(this.loginUrl, form, { observe: 'response' })

        request.subscribe({
            next: (value: HttpResponse<tokens>) => { UserService.accessToken = value.body?.access_token!!; this.changeRoute() },
            error: err => { this.notification.showError(err); }
        });
    }

    private changeRoute() {
        this.router.navigate(['/home']);
    }
}
