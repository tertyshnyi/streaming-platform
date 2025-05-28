import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormMovieComponent} from '../../components/admin-component/form-movie/form-movie.component';
import {FormBuilder, FormGroup } from '@angular/forms';
import { MediaService } from '../../../core/services/media.service';

@Component({
  selector: 'app-edit-movie',
  standalone: true,
  imports: [
    CommonModule,
    FormMovieComponent
  ],
  templateUrl: './edit-movie.component.html'
})
export class EditMovieComponent implements OnInit {
  @Input() id!: number;
  movieForm!: FormGroup;

  constructor(private mediaService: MediaService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.mediaService.getMovieById(this.id).subscribe(movie => {
      this.movieForm = this.fb.group({
        title: [movie.title],
        releaseDate: [movie.releaseDate],
        description: [movie.description],
        duration: [movie.duration],
        genres: [movie.genres],
        actors: [movie.actors],
        directors: [movie.directors],
        countries: [movie.countries],
        trailerUrl: [movie.trailerUrl],
        posterImg: [movie.posterImg],
        coverImg: [movie.coverImg],
        videos: [movie.videos || []],
        globalRating: [movie.globalRating]
      });
    });
  }

  onSave(): void {
    if (this.movieForm.valid) {
      this.mediaService.updateMovie(this.id, this.movieForm.value).subscribe(() => {
      });
    }
  }
}

