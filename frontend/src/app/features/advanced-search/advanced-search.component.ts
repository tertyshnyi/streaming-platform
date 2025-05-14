import { Component, HostListener, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

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
export class AdvancedSearchComponent {
  typeOptions: string[] = ['Всі', 'Фільми', 'Серіали'];
  noveltyOptions: string[] = ['👀 За обговоренням', '⭐ За рейтингом', '🆕 Нещодавно додано'];
  genres: string[] = ['Аніме', 'Фантастика', 'Комедія', 'Драма'];
  years: string[] = ['2020', '2021', '2022', '2023', '2024'];
  countries: string[] = ['Японія', 'США', 'Корея'];

  ngOnInit() {
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

  minRating: number = 0;
  maxRating: number = 10;

  onRatingChange() {
    console.log(`Рейтинг изменен: ${this.minRating} - ${this.maxRating}`);
  }

  shows: MediaCardModel[] = [
    {
      title: 'Андор',
      rating: 8.42,
      image: '/images/settings-background.jpg',
      genres: ['Драма', 'Аніме'],
      countries: ['Japan', 'Korea', 'Аніме', 'Аніме'],
      year: '2022',
    },
    {
      title: 'Аніме 1',
      rating: 7.91,
      image: '/images/settings-background.jpg',
      genres: ['Аніме'],
      countries: ['Germany', 'USA'],
      year: '2021',
    },
    {
      title: 'Аніме 2',
      rating: 6.89,
      image: '/images/settings-background.jpg',
      genres: ['Аніме', 'Фантастика'],
      countries: ['Germany'],
      year: '2020',
    },
    {
      title: 'Аніме 3',
      rating: 4.96,
      image: '/images/settings-background.jpg',
      genres: ['Аніме', 'Комедія'],
      countries: ['Korea'],
      year: '2023',
    },
  ];

  selectedTypeOption: string = '';
  selectedNoveltyOption: string = '';

  selectedGenres: { [key: string]: boolean } = {};
  selectedYears: { [key: string]: boolean } = {};
  selectedCountries: { [key: string]: boolean } = {};

  genreDropdownOpen: boolean = false;
  yearDropdownOpen: boolean = false;
  countryDropdownOpen: boolean = false;
  typeDropdownOpen: boolean = false;
  noveltyDropdownOpen: boolean = false;

  get selectedGenresList(): string[] {
    return Object.entries(this.selectedGenres)
      .filter(([_, checked]) => checked)
      .map(([genre]) => genre);
  }

  get selectedYearsList(): string[] {
    return Object.entries(this.selectedYears)
      .filter(([_, checked]) => checked)
      .map(([year]) => year);
  }

  get selectedCountriesList(): string[] {
    return Object.entries(this.selectedCountries)
      .filter(([_, checked]) => checked)
      .map(([country]) => country);
  }

  onGenreSelectionChanged(selectedGenres: string[]) {
    this.selectedGenres = selectedGenres.reduce((acc, genre) => {
      acc[genre] = true;
      return acc;
    }, {} as { [key: string]: boolean });
  }

  onYearSelectionChanged(selectedYears: string[]) {
    this.selectedYears = selectedYears.reduce((acc, year) => {
      acc[year] = true;
      return acc;
    }, {} as { [key: string]: boolean });
  }

  onCountrySelectionChanged(selectedCountries: string[]) {
    this.selectedCountries = selectedCountries.reduce((acc, country) => {
      acc[country] = true;
      return acc;
    }, {} as { [key: string]: boolean });
  }

  onTypeSelectionChanged(selectedTypes: string[]) {
      this.selectedTypeOption = selectedTypes[0] || '';
  }

  onNoveltySelectionChanged(selectedNovelty: string[]) {
    this.selectedNoveltyOption = selectedNovelty[0] || '';
  }

  toggleGenreDropdown() {
    this.genreDropdownOpen = !this.genreDropdownOpen;
    this.yearDropdownOpen = false;
    this.countryDropdownOpen = false;
    this.typeDropdownOpen = false;
    this.noveltyDropdownOpen = false;
  }

  toggleYearDropdown() {
    this.yearDropdownOpen = !this.yearDropdownOpen;
    this.genreDropdownOpen = false;
    this.countryDropdownOpen = false;
    this.typeDropdownOpen = false;
    this.noveltyDropdownOpen = false;
  }

  toggleCountryDropdown() {
    this.countryDropdownOpen = !this.countryDropdownOpen;
    this.genreDropdownOpen = false;
    this.yearDropdownOpen = false;
    this.typeDropdownOpen = false;
    this.noveltyDropdownOpen = false;
  }

  toggleTypeDropdown() {
    this.typeDropdownOpen = !this.typeDropdownOpen;
    this.noveltyDropdownOpen = false;
    this.genreDropdownOpen = false;
    this.yearDropdownOpen = false;
    this.countryDropdownOpen = false;
  }

  toggleNoveltyDropdown() {
    this.noveltyDropdownOpen = !this.noveltyDropdownOpen;
    this.typeDropdownOpen = false;
    this.genreDropdownOpen = false;
    this.yearDropdownOpen = false;
    this.countryDropdownOpen = false;
  }

  @HostListener('document:click', ['$event'])
  closeDropdowns(event: MouseEvent) {
    const target = event.target as HTMLElement;

    if (!target.closest('.custom-dropdown')) {
      this.genreDropdownOpen = false;
      this.yearDropdownOpen = false;
      this.countryDropdownOpen = false;
      this.typeDropdownOpen = false;
      this.noveltyDropdownOpen = false;
    }
  }

  applyFilters() {
    const queryParams = new URLSearchParams();

    if (this.selectedTypeOption) {
      queryParams.set('type', this.selectedTypeOption);
    }

    if (this.selectedGenresList.length > 0) {
      queryParams.set('genres', this.selectedGenresList.join(','));
    }

    if (this.selectedYearsList.length > 0) {
      queryParams.set('years', this.selectedYearsList.join(','));
    }

    if (this.selectedCountriesList.length > 0) {
      queryParams.set('countries', this.selectedCountriesList.join(','));
    }

    if (this.minRating !== 0 || this.maxRating !== 10) {
      queryParams.set('rating_min', this.minRating.toString());
      queryParams.set('rating_max', this.maxRating.toString());
    }

    if (this.selectedNoveltyOption) {
      queryParams.set('sort_by', this.selectedNoveltyOption);
    }

    const queryString = queryParams.toString();
    const newUrl = `/search?${queryString}`;

    this.router.navigateByUrl(newUrl);
    console.log('Фильтры применены, передан запрос на бекенд!', newUrl);
  }

  resetFilters() {
    this.selectedGenres = {};
    this.selectedYears = {};
    this.selectedCountries = {};
    this.selectedTypeOption = '';
    this.selectedNoveltyOption = '';
    this.minRating = 0;
    this.maxRating = 10;

    const slider = document.getElementById('rangeSlider') as any;
    if (slider && slider.noUiSlider) {
      slider.noUiSlider.set([this.minRating, this.maxRating]);
    }

    console.log('Фильтры сброшены!');
  }

  constructor(private router: Router) {}
}
