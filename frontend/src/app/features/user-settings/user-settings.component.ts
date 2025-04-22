import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettings {
  user = inject(UserService).getUserSignal();

  saveSettings(): void {
    console.log('Saved settings:', this.user());
  }

  onAvatarChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        const currentUser = this.user();
        if (currentUser) {
          // this.user.update((u: any) => ({
          //   ...u,
          //   profileImg: reader.result as string
          // }));
        }
      };

      reader.readAsDataURL(file);
    }
  }
}
