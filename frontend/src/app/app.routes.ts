import { Routes } from '@angular/router';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/login'
    },
    {
        path: 'login',
        loadComponent: () => {
            return import('./components/login/login').then(m => m.Login);
        }
    },
        {
        path: 'home',
        loadComponent: () => {
            return import('./components/home/home').then(m => m.Home);
        }
    },
];
