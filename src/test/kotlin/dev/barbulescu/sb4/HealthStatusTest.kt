package dev.barbulescu.sb4

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL
import org.springframework.test.json.JsonCompareMode.STRICT
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class HealthStatusTest(val webTestClient: WebTestClient) {

    @Test
    fun `actuator health is ok`() {
        val expectedBody = """
            {
  "components": {
    "diskSpace": {
      "status": "UP"
    },
    "livenessState": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    },
    "readinessState": {
      "status": "UP"
    },
    "ssl": {
      "status": "UP"
    },
    "custom": {
      "status": "UP"
    }
  },
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
"""
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(expectedBody, STRICT)

    }
}
