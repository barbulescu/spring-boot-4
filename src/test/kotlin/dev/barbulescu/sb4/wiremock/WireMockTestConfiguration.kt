package dev.barbulescu.sb4.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.any
import com.github.tomakehurst.wiremock.client.WireMock.anyUrl
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WireMockTestConfiguration(private val handlers: List<RequestHandler>) {

    @Bean(destroyMethod = "stop")
    fun wireMockServer(): WireMockServer {
        val transformer = DynamicWireMockTransformer(handlers)
        val server = WireMockServer(options().dynamicPort().extensions(transformer))
        server.start()
        server.stubFor(any(anyUrl()).willReturn(aResponse()))
        return server
    }
}
