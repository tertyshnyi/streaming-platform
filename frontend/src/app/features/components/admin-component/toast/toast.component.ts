import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-toast',
  standalone: true,
  templateUrl: './toast.component.html'
})
export class ToastComponent {
  constructor(private toast: ToastrService) {}

  showSuccess(str: string) {
    this.toast.success(str, 'Success');
  }

  showError(str: string) {
    this.toast.error(str, 'Error');
  }

  showInfo(str: string) {
    this.toast.info(str, 'Info');
  }

  showWarning(str: string) {
    this.toast.warning(str, 'Look');
  }
}
