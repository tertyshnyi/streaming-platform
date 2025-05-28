import { Component, inject, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../core/services/user.service';
import { SearchService } from '../../../core/services/search.service';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SearchResultModel } from '../../../core/models/search-result.model';

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
  private searchService = inject(SearchService);
  private userService = inject(UserService);
  user = this.userService.getUserSignal();

  readonly showAdminPanelButton = this.userService.hasAdminOrReleaserRole;

  logout() {
    this.userService.logout();
  }

  login() {
    this.userService.login();
  }

  goToRegister(): void {
    this.router.navigate(['/register']);
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

  searchResults: SearchResultModel[] = [];
  noResultsFound: boolean = false;

  onSearchChange() {
    const query = this.searchQuery.trim();

    if (query.length > 2) {
      this.searchService.search(query).subscribe({
        next: (results) => {
          this.searchResults = results;
          this.noResultsFound = results.length === 0;
        },
        error: (error) => {
          console.error('Search error:', error);
          this.searchResults = [];
          this.noResultsFound = false;
        }
      });
    } else {
      this.searchResults = [];
      this.noResultsFound = false;
    }
  }

  onResultClick() {
    this.clearSearch();
    this.isSearchFocused = false;
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
    this.searchResults = [];
    this.noResultsFound = false;
  }

  formatGenres(genres: string[] | undefined): string {
    if (!genres) return '';
    return genres
      .map(g => g.charAt(0).toUpperCase() + g.slice(1).toLowerCase())
      .join(', ');
  }
}
