import { Routes } from '@angular/router';
import { NotFoundComponent } from './features/pages/not-found/not-found.component';
import { UserSettings } from '../app/features/pages/user-settings/user-settings.component';
import { AuthGuard } from './core/guards/auth.guard';
import { WatchHistoriesComponent } from '../app/features/pages/watch-histories/watch-histories.component';
import { AdvancedSearchComponent } from './features/pages/advanced-search/advanced-search.component';
import { MediaContentComponent } from './features/pages/media-content/media-content.component';
import { RegistrationComponent } from './features/pages/registration/registration.component';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { AdminPanelComponent } from './features/pages/admin-panel/admin-panel.component';
import { CreateMovieComponent } from './features/pages/create-movie/create-movie.component';
import { CreateSeriesComponent } from './features/pages/create-series/create-series.component';
import { EditMovieComponent } from './features/pages/edit-movie/edit-movie.component';
import { EditSeriesComponent } from './features/pages/edit-series/edit-series.component';
import { RoleGuard } from '../app/core/guards/role.guard';
import { HomeComponent } from './features/pages/home/home.component';
import { MoviesComponent } from './features/pages/movies/movies.component';
import { SeriesComponent } from './features/pages/series/series.component';

export const routes: Routes = [
  // , canActivate: [RoleGuard]
  {path: '', component: HomeComponent},
  {path: 'admin', component: AdminPanelComponent},
  {path: 'admin/create-movie', component: CreateMovieComponent},
  {path: 'admin/create-series', component: CreateSeriesComponent},
  {path: 'admin/edit-movie/:id', component: EditMovieComponent},
  {path: 'admin/edit-series/:id', component: EditSeriesComponent},

  {path: 'settings', component: UserSettings, canActivate: [AuthGuard]},
  {path: 'watchlist', component: WatchHistoriesComponent, canActivate: [AuthGuard]},

  {path: 'search', component: AdvancedSearchComponent},
  {path: 'media/movies', component: MoviesComponent},
  {path: 'media/series', component: SeriesComponent},
  {path: 'media/:type/:id', component: MediaContentComponent},

  {path: 'register', component: RegistrationComponent, canActivate: [NoAuthGuard]},

  {path: '', redirectTo: 'search', pathMatch: 'full'},
  {path: '**', component: NotFoundComponent}
];
