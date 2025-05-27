import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../services/user.service';

export const RoleGuard: CanActivateFn = async () => {
  const userService = inject(UserService);
  const router = inject(Router);

  const user = await userService.tryLogin();

  if (user && userService.hasRole('ADMIN', 'RELEASER')) {
    return true;
  }

  router.navigate(['/no-access']);
  return false;
};
