import { Routes } from '@angular/router';
import { NotFoundComponent } from './features/pages/not-found/not-found.component';
import { UserSettings } from '../app/features/pages/user-settings/user-settings.component';
import { AuthGuard } from './core/guards/auth.guard';
import { WatchHistoriesComponent } from '../app/features/pages/watch-histories/watch-histories.component';
import { AdvancedSearchComponent } from './features/pages/advanced-search/advanced-search.component';
import { MediaContentComponent } from './features/pages/media-content/media-content.component';

export const routes: Routes = [
  {path: 'settings', component: UserSettings, canActivate: [AuthGuard]},
  {path: 'watchlist', component: WatchHistoriesComponent, canActivate: [AuthGuard]},
  {path: 'search', component: AdvancedSearchComponent},
  {path: 'media/:slug', component: MediaContentComponent},

  {path: '', redirectTo: '', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
];
