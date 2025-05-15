import { Component, HostListener, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { MediaService } from '../../core/services/media-content.service';
import { MediaCardModel } from '../../core/models/media-card.model';

import { MediaCardComponent } from '../../shared/components/media-card/media-card.component';
import { DropdownComponent } from '../../shared/components/dropdown-filter/dropdown-filter.component';
import { SparklesComponent } from '../../shared/components/sparkles/sparkles.component';

import noUiSlider from 'nouislider';

@Component({
  selector: 'app-advanced-search',
  standalone: true,
  imports: [
    FormsModule,
    MediaCardComponent,
    DropdownComponent,
    SparklesComponent,
    RouterModule,
    CommonModule
  ],
  templateUrl: './advanced-search.component.html',
  styleUrls: ['./advanced-search.component.scss'],
})
export class AdvancedSearchComponent implements OnInit {
  shows: MediaCardModel[] = [];
  filteredShows: MediaCardModel[] = [];

  typeOptions = ['All', 'Movies', 'TV Shows'];
  noveltyOptions = ['✨ By Novelty', '👀 Most Discussed', '⭐ Highest Rated', '🆕 Recently Added'];
  genres = ['Anime', 'Sci-Fi', 'Comedy', 'Drama'];
  years = ['2020', '2021', '2022', '2023', '2024'];
  countries = ['Japan', 'USA', 'Korea'];

  selectedTypeOption = '';
  selectedNoveltyOption = '';
  selectedGenres: { [key: string]: boolean } = {};
  selectedYears: { [key: string]: boolean } = {};
  selectedCountries: { [key: string]: boolean } = {};
  minRating = 0;
  maxRating = 10;

  dropdownState: { [key: string]: boolean } = {
    genre: false,
    year: false,
    country: false,
    type: false,
    novelty: false
  };

  constructor(private mediaService: MediaService, private router: Router) {}

  ngOnInit(): void {
    this.mediaService.getMedia().subscribe(data => {
      console.log('Fetched media:', data);
      this.shows = data;
      this.filteredShows = [...this.shows];
    });

    const rangeSlider = document.getElementById('rangeSlider') as HTMLElement;

    if (rangeSlider) {
      const slider = rangeSlider as HTMLElement & { noUiSlider: any };

      noUiSlider.create(slider, {
        start: [this.minRating, this.maxRating],
        tooltips: false,
        connect: [true, true, true],
        range: {
          min: 0,
          max: 10,
        },
        step: 1
      });

      slider.noUiSlider.on('update', (values: [string, string], handle: number) => {
        this.minRating = parseInt(values[0]);
        this.maxRating = parseInt(values[1]);

        const handles = rangeSlider.querySelectorAll('.noUi-handle');
        handles.forEach((handleEl, index) => {
          handleEl.innerHTML = `<span class="slider-value">${Math.round(+values[index])}</span>`;
        });
      });
    }
  }

  get selectedGenresList(): string[] {
    return Object.entries(this.selectedGenres)
      .filter(([_, checked]) => checked)
      .map(([key]) => key);
  }

  applyFilters(): void {
    this.filteredShows = this.shows.filter(show =>
      (this.selectedTypeOption === 'All' || !this.selectedTypeOption || show.title.includes(this.selectedTypeOption)) &&
      (this.selectedGenresList.length === 0 || this.selectedGenresList.some(g => show.genres.includes(g))) &&
      (this.selectedYearsList.length === 0 || this.selectedYearsList.includes(show.year?.toString() ?? '')) &&
      (this.selectedCountriesList.length === 0 || this.selectedCountriesList.some(c => show.countries?.includes(c))) &&
      show.globalRating >= this.minRating && show.globalRating <= this.maxRating
    );

    console.log('Filtered Shows:', this.filteredShows);
  }

  resetFilters(): void {
    this.selectedGenres = {};
    this.selectedYears = {};
    this.selectedCountries = {};
    this.selectedTypeOption = '';
    this.selectedNoveltyOption = '';
    this.minRating = 0;
    this.maxRating = 10;
    this.filteredShows = [...this.shows];

    const slider = document.getElementById('rangeSlider') as any;
    if (slider?.noUiSlider) {
      slider.noUiSlider.set([this.minRating, this.maxRating]);
    }

    console.log('Filters reset!');
  }

  onGenreSelectionChanged(selected: string[]) {
    this.selectedGenres = Object.fromEntries(selected.map(g => [g, true]));
  }

  get selectedYearsList(): string[] {
    return Object.entries(this.selectedYears)
      .filter(([_, checked]) => checked)
      .map(([key]) => key);
  }

  get selectedCountriesList(): string[] {
    return Object.entries(this.selectedCountries)
      .filter(([_, checked]) => checked)
      .map(([key]) => key);
  }

  onYearSelectionChanged(selected: string[]) {
    this.selectedYears = Object.fromEntries(selected.map(y => [y, true]));
  }

  onCountrySelectionChanged(selected: string[]) {
    this.selectedCountries = Object.fromEntries(selected.map(c => [c, true]));
  }

  onTypeSelectionChanged(selected: string[]) {
    this.selectedTypeOption = selected[0] || '';
  }

  onNoveltySelectionChanged(selected: string[]) {
    this.selectedNoveltyOption = selected[0] || '';
  }

  toggleDropdown(name: string): void {
    for (const key in this.dropdownState) {
      this.dropdownState[key] = key === name ? !this.dropdownState[key] : false;
    }
  }

  @HostListener('document:click', ['$event'])
  closeDropdowns(event: MouseEvent): void {
    const target = event.target as HTMLElement;
    if (!target.closest('.custom-dropdown')) {
      for (const key in this.dropdownState) {
        this.dropdownState[key] = false;
      }
    }
  }
}
