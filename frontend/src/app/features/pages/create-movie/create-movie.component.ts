import {Component} from '@angular/core';
import {FormMovieComponent} from '../../components/admin-component/form-movie/form-movie.component';

@Component({
  selector: 'app-create-movie',
  standalone: true,
  imports: [
    FormMovieComponent
  ],
  templateUrl: './create-movie.component.html'
})
export class CreateMovieComponent {

}
