import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'user-settings',
    loadComponent: () =>
      import('./features/users/components/user-settings/user-settings.component')
        .then(m => m.UserSettingsComponent)
  },
  {
    path: '',
    redirectTo: 'user-settings',
    pathMatch: 'full'
  }
];
