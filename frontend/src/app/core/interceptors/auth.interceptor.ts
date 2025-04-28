import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private oauthService: OAuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.oauthService.getAccessToken();

    if (token) {
      const newReq = req.clone({
        headers: req.headers.set('Authorization', 'Bearer ' + token)
      });

      return next.handle(newReq);
    }

    return next.handle(req);
  }
}
