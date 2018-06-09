import {ChangeDetectionStrategy, Component, HostBinding, OnDestroy} from '@angular/core';
import {ObservableMedia} from '@angular/flex-layout';
import {Title} from '@angular/platform-browser';
import {Store} from '@ngrx/store';
import {TranslateService} from '@ngx-translate/core';
import {Subscription} from 'rxjs/Subscription';
import {UtilService} from '../../services/util.service';

@Component({
  selector: 'jfundsvr-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AppComponent implements OnDestroy {
  @HostBinding('class.jfundsvr-page') b1 = true;
  @HostBinding('class.jfundsvr-root') b2 = true;
  private _subscriptions: Subscription[] = [];

  constructor(private store: Store<any>,
              private media$: ObservableMedia,
              private translate: TranslateService,
              private title: Title,
              private utilService: UtilService) {
    translate.setDefaultLang('zh_CN');
    translate.use('zh_CN');
    this._subscriptions.push(
      this.translate.get('TITLE').subscribe(it => title.setTitle(it)),
    );
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach(it => it.unsubscribe());
  }

}
