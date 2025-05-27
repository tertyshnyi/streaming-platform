import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormSeriesComponent} from '../../components/admin-component/form-series/form-series.component';

@Component({
  selector: 'app-create-series',
  standalone: true,
  imports: [CommonModule, FormSeriesComponent],
  templateUrl: './create-series.component.html'
})
export class CreateSeriesComponent {

}
