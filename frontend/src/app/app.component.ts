import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FooterComponent } from './shared/components/footer/footer.component';
import { NavbarComponent } from './shared/components/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterModule,
    FooterComponent,
    NavbarComponent,
  ],
  templateUrl: './app.component.html'
})
export class AppComponent {}
