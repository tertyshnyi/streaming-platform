import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { MovieModel } from '../../../core/models/movie.model';
import { MediaService } from '../../../core/services/media.service';
import { MediaCardComponent } from '../../../shared/components/media-card/media-card.component';

@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    MediaCardComponent
  ],
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.scss']
})
export class MoviesComponent implements OnInit {
  movies: MovieModel[] = [];
  filteredShows: MovieModel[] = [];
  loading = true;
  errorLoading = false;

  constructor(private mediaService: MediaService) {}

  ngOnInit(): void {
    this.mediaService.getAllMovies().subscribe({
      next: (data) => {
        this.movies = data;
        this.filteredShows = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading movies', err);
        this.errorLoading = true;
        this.loading = false;
      }
    });
  }
}
