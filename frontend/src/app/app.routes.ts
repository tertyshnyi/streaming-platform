import {Routes} from '@angular/router';
import {NotFoundComponent} from './features/not-found/not-found.component';
import {UserSettings} from '../app/features/user-settings/user-settings.component';

export const routes: Routes = [
  {path: 'user-settings', component: UserSettings},

  {path: '', redirectTo: 'user-settings', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
];
