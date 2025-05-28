import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormSeriesComponent} from '../../components/admin-component/form-series/form-series.component';
import {FormBuilder, FormGroup } from '@angular/forms';
import { MediaService } from '../../../core/services/media.service';

@Component({
  selector: 'app-edit-series',
  standalone: true,
  imports: [
    CommonModule,
    FormSeriesComponent
  ],
  templateUrl: './edit-series.component.html'
})
export class EditSeriesComponent implements OnInit {
  @Input() id!: number;
  seriesForm!: FormGroup;

  constructor(private mediaService: MediaService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.mediaService.getSeriesById(this.id).subscribe(series => {
      this.seriesForm = this.fb.group({
        title: [series.title],
        slug: [series.slug],
        releaseDate: [series.releaseDate],
        description: [series.description],
        avgDuration: [series.avgDuration],
        episodesCount: [series.episodesCount],
        genres: [series.genres],
        actors: [series.actors],
        directors: [series.directors],
        countries: [series.countries],
        posterImg: [series.posterImg],
        coverImg: [series.coverImg],
        episodes: [series.episodes || []],
        trailerUrl: [series.trailerUrl],
      });
    });
  }

  onSave(): void {
    if (this.seriesForm.valid) {
      this.mediaService.updateSeries(this.id, this.seriesForm.value).subscribe(() => {
      });
    }
  }
}
