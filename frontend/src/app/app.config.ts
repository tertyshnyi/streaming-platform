import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AuthInterceptor } from '../app/core/interceptors/auth.interceptor';
import localeSk from '@angular/common/locales/sk';
import { registerLocaleData } from '@angular/common';
import { DefaultOAuthInterceptor, OAuthModule } from 'angular-oauth2-oidc';
import { routes } from './app.routes';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideToastr } from 'ngx-toastr';

registerLocaleData(localeSk, 'sk');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideAnimations(),
    provideHttpClient(
      withInterceptorsFromDi()
    ),
    provideToastr({
      positionClass: 'toast-top-right',
      timeOut: 4000,
      preventDuplicates: true,
    }),
    importProvidersFrom(
      OAuthModule.forRoot({
        resourceServer: {
          sendAccessToken: true
        },
      })
    ),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: DefaultOAuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ]
};
