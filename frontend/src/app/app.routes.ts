import { Routes } from '@angular/router';
import { NotFoundComponent } from './features/not-found/not-found.component';
import { UserSettings } from '../app/features/user-settings/user-settings.component';
import { AuthGuard } from './core/guards/auth.guard';
import { WatchHistoriesComponent } from '../app/features/watch-histories/watch-histories.component';

export const routes: Routes = [
  {path: 'user-settings', component: UserSettings, canActivate: [AuthGuard]},
  {path: 'history', component: WatchHistoriesComponent, canActivate: [AuthGuard]},

  // {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
];
