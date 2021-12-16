import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { loginInfo } from 'src/models/loginInfo';
import { NotificationService } from './notification.service';
import { tokens } from 'src/models/tokens';


@Injectable({
  providedIn: 'root'
})
export class UserService {

    private readonly loginUrl = 'http://localhost:8080/api/v1/login';
    private static accessToken = ""

    constructor(private http: HttpClient, private notification: NotificationService) { }

    static getAccessToken() : string
    {
        return this.accessToken;
    }

    login(user: loginInfo)
    {
        const headers = new HttpHeaders()
            .append(
                'Content-Type',
                'application/json'
            );

        var request = this.http.post<tokens>(this.loginUrl, user, {headers: headers, observe: 'response'})
        request.subscribe({
            next: (value: HttpResponse<tokens>) => { UserService.accessToken = value.body?.access_token!! },
            error: err => { this.notification.showError(err); }
        });
    }
}
