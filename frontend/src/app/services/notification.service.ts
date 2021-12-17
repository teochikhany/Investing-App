import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from './user.service';


@Injectable({
  providedIn: 'root'
})
export class NotificationService {

    constructor(private snackBar: MatSnackBar) { }

    showError(err: any)
    {
        if (err.status == 0)
        {
            this.snackBar.open("Cannot connect to Server", "Dismiss");
        }
        else if (err.error.message === undefined)
        {
            const error : string = err.error.error;

            this.snackBar.open(error, "Dismiss");

            if (error.startsWith("The Token has expired on"))
            {
                UserService.refreshToken();
            }
        }
        else
        {
            this.snackBar.open(err.error.message, "Dismiss");
        }

    }
}
