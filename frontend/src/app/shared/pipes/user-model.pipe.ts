import {Pipe, PipeTransform} from '@angular/core';
import {UserModel} from '../../core/models/user-model';

@Pipe({
  name: 'userModel'
})
export class UserModelPipe implements PipeTransform {

  transform(value: UserModel | undefined): string {
    if (!value) {
      return '';
    }

    return value.name;
  }

}
