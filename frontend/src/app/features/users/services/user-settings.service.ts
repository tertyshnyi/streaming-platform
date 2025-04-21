import { Injectable } from '@angular/core';
import { UserSettings } from '../models/user-settings.model';

@Injectable({
  providedIn: 'root',
})
export class UserSettingsService {

  constructor() {}

  getDefaultSettings(): UserSettings {
    return {
      name: 'Volodymyr Tertyshnyi',
      email: 'tertyshnyiv@gmail.com',
      password: '',
      phoneNumber: '+380956848700',
      profileImg: 'https://www.hollywoodreporter.com/wp-content/uploads/2012/12/img_logo_blue.jpg?w=2000&h=1126&crop=1'
    };
  }
}
