import { Component, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../core/services/user.service';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SearchResult } from '../../../core/models/search-result';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    RouterLink,
    RouterLinkActive,
    CommonModule,
    FormsModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  private router = inject(Router);

  userService = inject(UserService);
  user = this.userService.getUserSignal();

  logout() {
    this.userService.logout();
  }

  login() {
    this.userService.login();
  }

  previousScrollTop = 0;
  showBottomBar = true;

  @HostListener('window:scroll', [])
  onWindowScroll() {
    const currentScroll = window.scrollY;

    if (currentScroll > this.previousScrollTop + 10) {
      this.showBottomBar = false;
    } else if (currentScroll < this.previousScrollTop - 10) {
      this.showBottomBar = true;
    }

    this.previousScrollTop = currentScroll;
  }

  searchQuery: string = '';
  isSearchFocused: boolean = false;

  searchResults: SearchResult[] = [];
  noResultsFound: boolean = false;

  onSearchChange() {
    if (this.searchQuery.length > 2) {
      const allResults: SearchResult[] = [
        {
          image: '/images/settings-background.jpg',
          title: 'Movie 1',
          description: 'This is a description of Movie 1.',
          rating: 8.5,
          genre: 'Action'
        },
        {
          image: '/images/settings-background.jpg',
          title: 'Movie 2',
          description: 'This is a description of Movie 2.',
          rating: 7.0,
          genre: 'Comedy'
        },
        {
          image: '/images/settings-background.jpg',
          title: 'Movie 3',
          description: 'This is a description of Movie 3.',
          rating: 6.5,
          genre: 'Drama'
        }
      ];

      this.searchResults = allResults.filter(result =>
        result.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        result.genre.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        result.description.toLowerCase().includes(this.searchQuery.toLowerCase())
      );

      this.noResultsFound = this.searchResults.length === 0;
    } else {
      this.searchResults = [];
      this.noResultsFound = false;
    }
  }

  onSearchBlur() {
    setTimeout(() => {
      this.isSearchFocused = false;
    }, 150);
  }

  goToSearch() {
    const query = this.searchQuery.trim();
    if (query.length > 0) {
      this.router.navigate(['/search'], { queryParams: { q: query } });
    }
  }


  clearSearch() {
    this.searchQuery = '';
  }
}
