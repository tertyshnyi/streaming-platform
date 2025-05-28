import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MediaCardModel } from '../../../core/models/media-card.model';

@Component({
  selector: 'app-media-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './media-card.component.html',
  styleUrls: ['./media-card.component.scss'],
})
export class MediaCardComponent {
  @Input() media!: MediaCardModel;

  get ratingColorClass(): string {
    if (!this.media || !this.media.globalRating) {
      return 'rating-gray';
    }
    const r = this.media.globalRating;
    if (r < 3) return 'rating-red';
    if (r < 5) return 'rating-orange';
    if (r < 8) return 'rating-yellow';
    return 'rating-green';
  }

  formatGenres(genres: string[]): string {
    if (!genres || genres.length === 0) return 'Unspecified';

    return genres
      .map(g => g.charAt(0).toUpperCase() + g.slice(1).toLowerCase())
      .join(', ');
  }
}
export type { MediaCardModel };
