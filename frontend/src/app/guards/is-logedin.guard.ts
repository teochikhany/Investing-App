import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { NotificationService } from '../services/notification.service';
import { UserService } from '../services/user.service';

@Injectable({
    providedIn: 'root'
})
export class IsLogedinGuard implements CanActivate {

    constructor(private router: Router,
                private notification: NotificationService,
                private userService: UserService) {}

    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): | boolean
    {
        const isLogedin = this.userService.isAuthenticated();

        if (!isLogedin)
        {
            this.router.navigate(["/login"])
            this.notification.showMessage("Please login first");
        }

        return isLogedin;
    }

}
