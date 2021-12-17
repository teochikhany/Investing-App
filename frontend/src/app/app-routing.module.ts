import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from 'src/app/modules/homepage/homepage.component';
import { LoginComponent } from './modules/login/login.component';
import { PageNotFoundComponent } from 'src/app/modules/page-not-found/page-not-found.component';
import { SignupComponent } from './modules/signup/signup.component';
import { IsLogedinGuard } from './guards/is-logedin.guard';

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
    { path: 'home', component: HomepageComponent, canActivate: [IsLogedinGuard] },
    { path: '',   redirectTo: '/login', pathMatch: 'full' },
    { path: '**', component: PageNotFoundComponent },
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
