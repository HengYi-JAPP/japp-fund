import {Pipe, PipeTransform} from '@angular/core';
import {Operator} from '../models/operator';

@Pipe({
  name: 'operatorAvatar'
})
export class OperatorAvatarPipe implements PipeTransform {
  static DEFAULT_AVATAR = 'http://www.getuikit.net/docs/images/placeholder_avatar.svg';

  transform(operator: Operator): string {
    return operator && operator.avatar || OperatorAvatarPipe.DEFAULT_AVATAR;
  }

}
