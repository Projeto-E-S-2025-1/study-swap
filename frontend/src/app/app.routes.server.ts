// src/app/app.routes.server.ts
import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'login',
    renderMode: RenderMode.Server
  },
  {
    path: 'register',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'material/:id',
    renderMode: RenderMode.Server
  },
  {
    path: 'material/edit/:id',
    renderMode: RenderMode.Server
  },
  {
    path: 'material/**',
    renderMode: RenderMode.Server
  },
  {
    path: 'report',
    renderMode: RenderMode.Server
  },
  {
    path: 'assets/**',
    renderMode: RenderMode.Prerender
  },
  {
    path: '**',
    renderMode: RenderMode.Client
  }
];
