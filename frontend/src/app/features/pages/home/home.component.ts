import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { MediaContentService } from '../../../core/services/media-content.service';
import { MediaHomeCardModel } from '../../../core/models/media-home-card.model';

@Component({
  selector: 'app-media-slider',
  imports: [CommonModule, RouterModule],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  mediaList: MediaHomeCardModel[] = [];
  currentSlide = 0;
  intervalId: any;

  constructor(private mediaService: MediaContentService, private router: Router) {}

  ngOnInit(): void {
    this.mediaService.getLatestMedia().subscribe((data) => {
      this.mediaList = data;
      this.startAutoSlide();
    });
  }

  onArrowClick(direction: 'left' | 'right') {
    this.stopAutoSlide();
    if (direction === 'left') {
      this.prevSlide();
    } else {
      this.nextSlide();
    }
    this.startAutoSlide();
  }

  get activeSlide() {
    return this.mediaList[this.currentSlide];
  }

  startAutoSlide() {
    this.intervalId = setInterval(() => this.nextSlide(), 10000);
  }

  stopAutoSlide() {
    clearInterval(this.intervalId);
  }

  nextSlide() {
    if (this.mediaList.length) {
      this.currentSlide = (this.currentSlide + 1) % this.mediaList.length;
    }
  }

  prevSlide() {
    if (this.mediaList.length) {
      this.currentSlide = (this.currentSlide - 1 + this.mediaList.length) % this.mediaList.length;
    }
  }

  goToSlide(index: number) {
    this.currentSlide = index;
  }

  navigateTo(slug: string, type: string, id: number) {
    this.router.navigate(['/media', type.toLowerCase(), id]);
  }
}
