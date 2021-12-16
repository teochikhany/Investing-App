import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomepageComponent } from 'src/app/modules/homepage/homepage.component';
import { LoginComponent } from './modules/login/login.component';
import { PageNotFoundComponent } from 'src/app/modules/page-not-found/page-not-found.component';
import { SignupComponent } from './modules/signup/signup.component';

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'home', component: HomepageComponent },
    { path: 'signup', component: SignupComponent },
    { path: '',   redirectTo: '/login', pathMatch: 'full' },
    { path: '**', component: PageNotFoundComponent },
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
