package eu.derfniw.resources

import eu.derfniw.resources.jsonschema.PublicConfig
import eu.derfniw.resources.jsonschema.UIMode
import eu.derfniw.resources.jsonschema.UserConfig
import io.quarkus.security.Authenticated
import jakarta.annotation.security.PermitAll
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.NoCache

@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
@NoCache
class ConfigResource {

    @GET
    @Path("/public")
    @PermitAll
    fun getPublicConfig(): PublicConfig = PublicConfig("DEV!")

    @GET
    @Path("/user")
    fun getUserConfig(): UserConfig = UserConfig(UIMode.SYSTEM)
}