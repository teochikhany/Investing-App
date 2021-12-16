import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';


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
        else
        {
            this.snackBar.open(err.error.message, "Dismiss");
        }

    }
}
