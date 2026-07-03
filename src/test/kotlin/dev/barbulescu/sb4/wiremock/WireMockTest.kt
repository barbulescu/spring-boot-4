package dev.barbulescu.sb4.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import dev.barbulescu.sb4.wiremock.example.GreetingRequestHandler
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import kotlin.test.assertEquals

@SpringBootTest(
    classes = [WireMockTestConfiguration::class, GreetingRequestHandler::class],
    webEnvironment = NONE
)
@TestConstructor(autowireMode = ALL)
class WireMockTest(private val wireMockServer: WireMockServer) {

    private val client by lazy { RestClient.create(wireMockServer.baseUrl()) }

    @Test
    fun `greeting handler returns Hello message`() {
        val body = client.get().uri("/greet/world")
            .retrieve()
            .body<String>()
        assertEquals("Hello, world!", body)
    }

    @Test
    fun `unmatched request returns 404`() {
        val response = client.get().uri("/unknown")
            .retrieve()
            .onStatus({ it.is4xxClientError }) { _, _ -> }
            .toBodilessEntity()
        assertEquals(404, response.statusCode.value())
    }
}
