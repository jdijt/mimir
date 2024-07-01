package eu.derfniw.resources

import eu.derfniw.Config
import eu.derfniw.resources.jsonschema.AppProfile
import eu.derfniw.resources.jsonschema.PublicConfig
import eu.derfniw.resources.jsonschema.UIMode
import eu.derfniw.resources.jsonschema.UserConfig
import io.quarkus.security.Authenticated
import jakarta.annotation.security.PermitAll
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.NoCache

@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@NoCache
class ConfigResource(val config: Config, @ConfigProperty(name = "quarkus.profile") val quarkusProfile: String) {

    @GET
    @Path("/public")
    @PermitAll
    fun getPublicConfig(): PublicConfig = PublicConfig(
        config.name(),
        config.frontendConfig().oidcClientId(),
        config.frontendConfig().oidcAuthServerUrl().toString(),
        if (quarkusProfile == "dev") AppProfile.DEV else AppProfile.PROD
    )

    @GET
    @Path("/user")
    @Authenticated
    fun getUserConfig(): UserConfig = UserConfig(UIMode.SYSTEM)

}