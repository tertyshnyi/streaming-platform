import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sparkles',
  standalone: true,
  imports: [ CommonModule ],
  templateUrl: './sparkles.component.html',
  styleUrls: ['./sparkles.component.scss']
})
export class SparklesComponent implements OnInit {

  sparkles: { top: number; left: number; delay: number }[] = [];

  ngOnInit(): void {
    const pageHeight = Math.max(
      document.documentElement.scrollHeight,
      document.body.scrollHeight,
      document.documentElement.clientHeight
    );
    const pageWidth = Math.max(
      document.documentElement.scrollWidth,
      document.body.scrollWidth,
      document.documentElement.clientWidth
    );

    this.sparkles = Array.from({ length: 50 }, () => ({
      top: Math.random() * pageHeight,
      left: Math.random() * pageWidth,
      delay: Math.random() * 3
    }));
  }
}
