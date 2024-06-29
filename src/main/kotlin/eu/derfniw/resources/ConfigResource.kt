package eu.derfniw.resources

import eu.derfniw.resources.jsonschema.PublicConfig
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
class ConfigResource {

    @GET
    @Path("/public")
    fun getPublicConfig(): PublicConfig =
}