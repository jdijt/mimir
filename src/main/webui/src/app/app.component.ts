import {Component} from '@angular/core';
import {GlobalConfigService} from "./global-config.service";
import {HttpClient} from "@angular/common/http";
import {OAuthService} from "angular-oauth2-oidc";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
})
export class AppComponent {
  title: string
  userConfig: string = ""
  userName: string = ""

  constructor(configService: GlobalConfigService, client: HttpClient, auth: OAuthService) {
    this.title = configService.getInstanceName()

    client
      .get('/api/config/user', {headers: {"Authorization": `Bearer ${auth.getAccessToken()}`}})
      .subscribe((config: any) => this.userConfig = JSON.stringify(config, null, 2))

    this.userName = auth.getIdentityClaims()["name"]
  }

}
