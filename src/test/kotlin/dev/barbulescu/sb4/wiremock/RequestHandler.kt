package dev.barbulescu.sb4.wiremock

import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.verification.LoggedRequest

fun interface RequestHandler {
    fun handle(request: LoggedRequest): ResponseDefinition?
}
