import { Component } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'investing-frontend';

    opened: boolean = false;

    logout()
    {
        UserService.clearTokens();
    }
}
