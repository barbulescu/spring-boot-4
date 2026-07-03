package dev.barbulescu.sb4.wiremock

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.stubbing.ServeEvent

class DynamicWireMockTransformer(
    private val handlers: List<RequestHandler>
) : ResponseDefinitionTransformerV2 {

    override fun getName() = "dynamic-transformer"

    override fun applyGlobally() = true

    override fun transform(serveEvent: ServeEvent): ResponseDefinition =
        handlers.firstNotNullOfOrNull { it.handle(serveEvent.request) }
            ?: ResponseDefinitionBuilder.responseDefinition()
                .withStatus(404)
                .withBody("No handler matched: ${serveEvent.request.method} ${serveEvent.request.url}")
                .build()
}
