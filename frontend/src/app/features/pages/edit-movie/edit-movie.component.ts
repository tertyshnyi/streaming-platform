import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormMovieComponent} from '../../components/admin-component/form-movie/form-movie.component';

@Component({
  selector: 'app-edit-movie',
  standalone: true,
  imports: [
    CommonModule,
    FormMovieComponent
  ],
  templateUrl: './edit-movie.component.html'
})
export class EditMovieComponent {

}
