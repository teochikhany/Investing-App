import { Component, OnInit } from '@angular/core';
import { signupinfo } from 'src/app/models/signupinfo';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

    user: signupinfo = { username: "", name: "", password: "" }


    constructor(private userService: UserService) { }

    ngOnInit(): void {
    }

    signup() {
        this.userService.createUser(this.user);
    }

}
