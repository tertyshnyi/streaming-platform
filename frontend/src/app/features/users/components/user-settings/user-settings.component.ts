import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {UserSettingsService} from '../../services/user-settings.service';

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.scss']
})
export class UserSettingsComponent implements OnInit {
  userSettings: { password: string; email: string; name: string; phoneNumber: string; profileImg: string | undefined } = {
    name: '',
    email: '',
    password: '',
    phoneNumber: '',
    profileImg: '',
  };

  constructor(private userSettingsService: UserSettingsService) {}
  ngOnInit(): void {
    this.loadUserSettings();
  }

  loadUserSettings(): void {
    this.userSettings = this.userSettingsService.getDefaultSettings();
  }

  saveSettings(): void {
    console.log('Saved settings: ', this.userSettings);
  }

  onAvatarChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.userSettings.profileImg = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }
}
