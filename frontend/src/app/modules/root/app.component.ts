import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'investing-frontend';

    constructor(private userService: UserService) {}

    opened: boolean = false;

    logout()
    {
        this.userService.clearTokens();
    }
}
