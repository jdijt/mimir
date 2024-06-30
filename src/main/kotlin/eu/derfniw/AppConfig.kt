package eu.derfniw

import io.smallrye.config.ConfigMapping
import java.net.URI

@ConfigMapping(prefix = "kb")
interface Config {
    fun name(): String

    fun frontendConfig(): FrontendConfig

    interface FrontendConfig {
        fun oidcClientId(): String
        fun oidcAuthServerUrl(): URI
    }
}