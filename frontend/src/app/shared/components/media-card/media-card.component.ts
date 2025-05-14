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
    const r = this.media.rating;
    if (r < 3) return 'rating-red';
    if (r < 5) return 'rating-orange';
    if (r < 8) return 'rating-yellow';
    return 'rating-green';
  }

}
