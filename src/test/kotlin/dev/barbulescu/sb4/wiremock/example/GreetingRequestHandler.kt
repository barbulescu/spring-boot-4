package dev.barbulescu.sb4.wiremock.example

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import dev.barbulescu.sb4.wiremock.RequestHandler
import org.springframework.stereotype.Component

@Component
class GreetingRequestHandler : RequestHandler {

    private val urlPattern = Regex("^/greet/(.+)$")

    override fun handle(request: LoggedRequest): ResponseDefinition? {
        if (request.method != RequestMethod.GET) return null
        val name = urlPattern.matchEntire(request.url)?.groupValues?.get(1) ?: return null
        return ResponseDefinitionBuilder.responseDefinition()
            .withStatus(200)
            .withHeader("Content-Type", "text/plain")
            .withBody("Hello, $name!")
            .build()
    }
}
