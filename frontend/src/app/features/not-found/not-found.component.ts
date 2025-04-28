import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-not-found',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss']
})
export class NotFoundComponent implements OnInit {
  sparkles: { top: number; left: number; delay: number }[] = [];

  ngOnInit(): void {
    this.sparkles = Array.from({ length: 50 }, () => ({
      top: Math.random() * window.innerHeight,
      left: Math.random() * window.innerWidth,
      delay: Math.random() * 3
    }));
  }
}

