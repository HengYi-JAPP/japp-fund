import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
  selector: 'jfundsvr-not-found-page',
  templateUrl: './not-found-page.component.html',
  styleUrls: ['./not-found-page.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class NotFoundPageComponent {
  constructor() {
  }
}
