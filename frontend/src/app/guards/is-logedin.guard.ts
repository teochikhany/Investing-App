import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../services/user.service';

@Injectable({
    providedIn: 'root'
})
export class IsLogedinGuard implements CanActivate {

    constructor(private router: Router) {}

    canActivate(route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): | boolean
    {
        const isLogedin = UserService.isAuthenticated();

        if (!isLogedin)
        {
            this.router.navigate(["/noaccess"])
        }

        return isLogedin;
    }

}
