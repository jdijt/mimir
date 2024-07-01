package eu.derfniw.resources.jsonschema

import kotlinx.serialization.Serializable

@Serializable
data class PublicConfig(
    val instanceName: String,
    val oidcClientId: String,
    val oidcAuthServerUrl: String,
    val appProfile: AppProfile
)

@Serializable
data class UserConfig(
    val uiMode: UIMode
)

@Serializable
enum class UIMode {
    LIGHT,
    DARK,
    SYSTEM,
}

@Serializable
enum class AppProfile {
    PROD, DEV
}