import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from 'src/app/modules/root/app.component';
import { StockTableComponent } from 'src/app/modules/stocks-table/stocks-table.component';
import { MatTableModule } from '@angular/material/table'
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog'
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { JwtModule } from "@auth0/angular-jwt";
import { AddStockComponent, AppAddDialoge } from 'src/app/modules/add-stock/add-stock.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input'
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { FormsModule } from '@angular/forms';
import { PriceValidatorDirective } from './validation/price-validator.directive';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { LoginComponent } from './modules/login/login.component';
import { HomepageComponent } from 'src/app/modules/homepage/homepage.component';
import { MatCardModule } from '@angular/material/card';
import { PageNotFoundComponent } from 'src/app/modules/page-not-found/page-not-found.component';
import { SignupComponent } from './modules/signup/signup.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { AuthInterceptorInterceptor } from './interceptors/auth-interceptor.interceptor';

export function getTokens()
{
    return localStorage.getItem("access_token");
}

@NgModule({
    declarations: [
        AppComponent,
        StockTableComponent,
        AddStockComponent,
        AppAddDialoge,
        PriceValidatorDirective,
        LoginComponent,
        HomepageComponent,
        PageNotFoundComponent,
        SignupComponent
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
        MatSidenavModule,
        MatDividerModule,
        JwtModule.forRoot({
            config: {
                tokenGetter: getTokens,
                allowedDomains: ["localhost:8080"],
                disallowedRoutes: ["http://localhost:8080/api/v1/user/refreshtoken",
                    "http://localhost:8080/api/v1/login"]
            },
        }),
    ],

    exports: [],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptorInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
