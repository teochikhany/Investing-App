import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './root/app.component';
import { StockTableComponent } from './stocks-table/stocks-table.component';
import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog'
import { HttpClientModule } from '@angular/common/http';
import { JwtModule } from "@auth0/angular-jwt";
import { AddStockComponent, AppAddDialoge } from './add-stock/add-stock.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FormsModule } from '@angular/forms';
import { PriceValidatorDirective } from './validation/price-validator.directive';
import { MatIconModule } from '@angular/material/icon';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { UserService } from 'src/services/user.service';
import { LoginComponent } from './login/login.component';
import { HomepageComponent } from './homepage/homepage.component';
import {MatCardModule} from '@angular/material/card';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

@NgModule({
    declarations: [
        AppComponent,
        StockTableComponent,
        AddStockComponent,
        AppAddDialoge,
        PriceValidatorDirective,
        LoginComponent,
        HomepageComponent,
        PageNotFoundComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        MatTableModule,
        MatButtonModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        HttpClientModule,
        BrowserAnimationsModule,
        MatIconModule,
        MatSnackBarModule,
        MatCardModule,
        JwtModule.forRoot({
            config: {
              tokenGetter: () => { return UserService.getAccessToken() },
              allowedDomains: ["http://localhost:8080/api/v1/**"]
            },
        }),
    ],

    exports: [],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
