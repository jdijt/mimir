import {APP_INITIALIZER, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {provideHttpClient, withFetch} from "@angular/common/http";
import {OAuthService, provideOAuthClient} from "angular-oauth2-oidc";
import {GlobalConfigService} from "./global-config.service";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [
    provideHttpClient(withFetch()),
    provideOAuthClient({
      resourceServer: {
        sendAccessToken: true,
        //TODO: Check if this is a secure way of going about it?
        allowedUrls: [`http://localhost:8080/api/*`]
      }
    }),
    GlobalConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: (cfg: GlobalConfigService, auth: OAuthService) => async () => {
        await cfg.init()
        return auth.loadDiscoveryDocumentAndLogin()
      },
      deps: [GlobalConfigService, OAuthService],
      multi: true
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
