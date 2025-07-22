// src/app/app.config.ts
import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
// CORREÇÃO AQUI: Mude 'auth-interceptor' para 'auth.interceptor'
import { authInterceptor } from './auth/auth-interceptor'; // Importe a função interceptor

// Se for usar JwtHelperService globalmente, configure aqui
// import { provideJwt } from '@auth0/angular-jwt'; // Se você quiser uma configuração mais robusta para JWT

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideClientHydration(withEventReplay()),
    provideHttpClient(withInterceptors([authInterceptor])),
    // Se você estiver usando @auth0/angular-jwt com uma configuração mais complexa, descomente e configure:
    // provideJwt({
    //   config: {
    //     tokenGetter: () => localStorage.getItem('token'),
    //     allowedDomains: ['localhost:8080'],
    //     disallowedRoutes: ['localhost:8080/auth/login', 'localhost:8080/auth/register'],
    //   },
    // }),
  ]
};