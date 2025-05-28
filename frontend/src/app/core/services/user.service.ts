import { computed, inject, Injectable, Signal, signal } from '@angular/core';
import { toObservable } from '@angular/core/rxjs-interop';
import { HttpClient } from '@angular/common/http';
import { UserModel } from '../models/user.model';
import { OAuthService } from 'angular-oauth2-oidc';
import { authCodeFlowConfig } from '../config/authCodeFlowConfig.config';
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private user = signal<UserModel | undefined>(undefined);
  private http = inject(HttpClient);
  private apiUrl = `${environment.beUrl}/users`;

  public isAuthenticatedSignal = computed(() => !!this.user());
  public isAuthenticated$ = toObservable(this.isAuthenticatedSignal);

  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(authCodeFlowConfig);
    this.tryLogin();
  }

  hasRole(...roles: string[]): boolean {
    const user = this.user();
    const authorities = user?.authorities ?? [];
    return roles.some(role => authorities.includes(role));
  }
  
  public hasAdminOrReleaserRole = computed(() => {
    return this.hasRole('ADMIN', 'RELEASER');
  });

  register(userData: any): Observable<UserModel> {
    return this.http.post<UserModel>(this.apiUrl, userData);
  }

  updateUser(user: UserModel): Observable<UserModel> {
    return this.http.put<UserModel>(`${this.apiUrl}/${user.id}`, user);
  }

  getUserSignal() {
    return this.user.asReadonly();
  }

  tryLogin() {
    return this.oauthService.loadDiscoveryDocumentAndTryLogin()
      .then(() => {
        console.log('Attempting to fetch user data');
        return this.http.get<UserModel>(`${this.apiUrl}/me`).toPromise();
      })
      .then((userModel) => {
        console.log('User data loaded from backend:', userModel);
        this.user.set(userModel);
        return userModel;
      })
      .catch(err => {
        console.error('Login or user fetch failed:', err);
        this.user.set(undefined);
        return undefined;
      });
  }

  login() {
    this.oauthService.loadDiscoveryDocumentAndLogin()
      .then(() => {
        this.user.set(this.oauthService.getIdentityClaims() as UserModel);
      });
  }

  logout() {
    this.oauthService.logOut();
    this.user.set(undefined);
  }

  isUserLoggedIn() {
    return this.tryLogin()
      .then((userModel: UserModel | undefined) => {
        return !!userModel;
      });
  }
}

export const canActiveHome: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  const router = inject(Router);
  const userService = inject(UserService);

  return userService.isUserLoggedIn()
    .then(value => {
      if (value) {
        return true;
      } else {
        userService.login();
        return false;
      }
    });
};
