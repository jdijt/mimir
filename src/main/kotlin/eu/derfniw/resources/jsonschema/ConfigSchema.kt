package eu.derfniw.resources.jsonschema

import kotlinx.serialization.Serializable

@Serializable
data class PublicConfig(
    val instanceName: String,
    val oidcClientId: String,
    val oidcAuthServerUrl: String
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