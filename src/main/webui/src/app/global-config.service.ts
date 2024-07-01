import {Injectable} from "@angular/core";
import {PublicConfig} from "../jsonmodel/ConfigModel";
import {HttpClient} from "@angular/common/http";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable({
  providedIn: 'root'
})
export class GlobalConfigService {

  private publicConfig?: PublicConfig;

  constructor(private http: HttpClient, private oauthService: OAuthService) {
  }

  async init(): Promise<void> {
    await this.http.get<PublicConfig>('/api/config/public').forEach((config: PublicConfig) => {
      this.publicConfig = config
    })
    if (!this.publicConfig) {
      throw new Error("Public config not initialized")
    }
    const oauthConfig = {
      clientId: this.publicConfig.oidcClientId,
      issuer: this.publicConfig.oidcAuthServerUrl,
      redirectUri: window.location.origin,
      responseType: "code",
      scope: "openid profile email offline_access",
      showDebugInformation: this.publicConfig.appProfile === "DEV"
    }
    this.oauthService.configure(oauthConfig)
    this.oauthService.setupAutomaticSilentRefresh()
    await this.oauthService.loadDiscoveryDocument()
  }

  public getInstanceName(): string {
    if (!this.publicConfig) {
      throw new Error("Public config not initialized")
    }
    return this.publicConfig.instanceName
  }
}
