import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '../../../core/services/user.service';

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettings {
  private userService = inject(UserService);
  user = this.userService.getUserSignal();

  selectedAvatar: string | ArrayBuffer | null = null;
  saveSettings(): void {
    const currentUser = this.user();
    if (currentUser) {
      this.userService.updateUser(currentUser).subscribe({
        next: updated => {
          console.log('User updated:', updated);
          alert('User settings saved successfully!');
        },
        error: err => {
          console.error('Update failed:', err);
          alert('Failed to save settings.');
        }
      });
    }
  }

  onAvatarChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.selectedAvatar = reader.result;
      };

      reader.readAsDataURL(file);
    }
  }
}
