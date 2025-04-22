import {ApplicationConfig, importProvidersFrom, LOCALE_ID, provideZoneChangeDetection} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi} from '@angular/common/http';
import {authInterceptor} from './auth.interceptor';
import localeSk from '@angular/common/locales/sk';
import {registerLocaleData} from '@angular/common';
import {DefaultOAuthInterceptor, OAuthModule} from 'angular-oauth2-oidc';

registerLocaleData(localeSk, 'sk');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({eventCoalescing: true}),
    provideRouter(routes),
    provideHttpClient(
      withInterceptorsFromDi()
      // withInterceptors([authInterceptor])
    ),
    importProvidersFrom(
      OAuthModule.forRoot({
        resourceServer: {
          sendAccessToken: true
        },
      })
    ),

    {provide: HTTP_INTERCEPTORS, useClass: DefaultOAuthInterceptor, multi: true}

    // { provide: LOCALE_ID, useValue: 'sk' }
  ]
};
