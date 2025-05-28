import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { SeriesModel } from '../../../core/models/series.model';
import { MediaService } from '../../../core/services/media.service';
import { MediaCardComponent } from '../../../shared/components/media-card/media-card.component';
import { SparklesComponent } from "../../../shared/components/sparkles/sparkles.component";

@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    HttpClientModule,
    MediaCardComponent,
    SparklesComponent
  ],
  templateUrl: './series.component.html',
  styleUrls: ['./series.component.scss']
})
export class SeriesComponent implements OnInit {
  movies: SeriesModel[] = [];
  filteredShows: SeriesModel[] = [];
  loading = true;
  errorLoading = false;

  constructor(private mediaService: MediaService) {}

  ngOnInit(): void {
    this.mediaService.getAllSeries().subscribe({
      next: (data) => {
        this.movies = data;
        this.filteredShows = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading series', err);
        this.errorLoading = true;
        this.loading = false;
      }
    });
  }
}
