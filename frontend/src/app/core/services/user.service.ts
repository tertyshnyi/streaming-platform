import {inject, Injectable, signal} from '@angular/core';
import {UserModel} from '../models/user-model';
import {OAuthService} from 'angular-oauth2-oidc';
import {authCodeFlowConfig} from '../config/authCodeFlowConfig.config';
import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private user = signal<UserModel | undefined>(undefined);

  constructor(private oauthService: OAuthService) {
    this.oauthService.configure(authCodeFlowConfig);
    this.tryLogin();
  }

  getUserSignal() {
    return this.user.asReadonly();
  }

  tryLogin() {
    return this.oauthService.loadDiscoveryDocumentAndTryLogin()
      .then(() => {
        const userModel = this.oauthService.getIdentityClaims() as UserModel;
        console.log('User data loaded:', userModel);
        this.user.set(userModel);
        return userModel;
      })
      .catch(err => {
        console.error('Login failed:', err);
        return undefined;
      });
  }

  login() {
    this.oauthService.loadDiscoveryDocumentAndLogin()
      .then(() => {
        this.user.set(this.oauthService.getIdentityClaims() as UserModel);
      })
  }

  logout() {
    this.oauthService.logOut();
    this.user.set(undefined);
  }

  isUserLoggedIn() {
    return this.tryLogin()
      .then((userModel : UserModel | undefined) => {
        return !!userModel;
      })
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
    })
};
