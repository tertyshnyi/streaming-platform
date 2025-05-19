import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SparklesComponent } from '../../../shared/components/sparkles/sparkles.component';

@Component({
  selector: 'app-not-found',
  imports: [
    CommonModule,
    SparklesComponent
  ],
  standalone: true,
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.scss']
})
export class NotFoundComponent {}

