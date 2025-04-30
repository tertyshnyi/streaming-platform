import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { UserService } from './core/services/user.service';
import { NgbDropdown, NgbDropdownMenu, NgbDropdownToggle } from '@ng-bootstrap/ng-bootstrap';
import { FooterComponent } from './shared/components/footer/footer.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    NgbDropdown,
    NgbDropdownToggle,
    NgbDropdownMenu,
    FooterComponent,
  ],
  standalone: true,
  templateUrl: './app.component.html'
})
export class AppComponent {

  userService = inject(UserService);

  user = this.userService.getUserSignal();

  logout() {
    this.userService.logout();
  }

  login() {
    this.userService.login();
  }
}
