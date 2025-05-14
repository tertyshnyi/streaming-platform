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
    this.sparkles = Array.from({ length: 50 }, () => ({
      top: Math.random() * window.innerHeight,
      left: Math.random() * window.innerWidth,
      delay: Math.random() * 3
    }));
  }

}
