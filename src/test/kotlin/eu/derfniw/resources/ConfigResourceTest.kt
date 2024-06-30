package eu.derfniw.resources

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsStringIgnoringCase
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@QuarkusTest
@TestHTTPEndpoint(ConfigResource::class)
class ConfigResourceTest {

    @OptIn(ExperimentalEncodingApi::class)
    private val invalidToken: String = Base64.encode("I AM DEFINITELY NOT A TOKEN".toByteArray())

    @Test
    fun testGetPublicConfig() {
        given()
            .`when`().get("/public")
            .then()
            .statusCode(200)
            .body("instanceName", containsStringIgnoringCase("test"))
    }

    @Test
    fun testGetPublicConfigWithBogusAuth() {
        given()
            .headers("Authorization", "Bearer $invalidToken")
            .`when`().get("/public")
            .then()
            .statusCode(200)
    }

    @Test
    @TestSecurity(user = "test", roles = [])
    fun testGetPublicConfigWithAuth() {
        given()
            .`when`().get("/public")
            .then()
            .statusCode(200)
    }

    @Test
    fun testGetUserConfigNoAuth() {
        given()
            .`when`().get("/user")
            .then()
            .statusCode(401)
    }

    @Test
    @TestSecurity(user = "test", roles = [])
    fun testGetUserConfigWithAuth() {
        given()
            .`when`().get("/user")
            .then()
            .statusCode(200)
    }
}