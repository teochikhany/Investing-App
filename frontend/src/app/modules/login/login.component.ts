import { Component, OnInit } from '@angular/core';
import { loginInfo } from 'src/app/models/loginInfo';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    user: loginInfo = {username : "", password : ""};

    constructor(private userService: UserService) { }

    ngOnInit(): void {
    }

    login()
    {
        this.userService.login(this.user);
    }

}
