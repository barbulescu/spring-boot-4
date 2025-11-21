package dev.barbulescu.sb4

import org.springframework.boot.health.contributor.Health
import org.springframework.boot.health.contributor.HealthIndicator
import org.springframework.stereotype.Component

@Component
class CustomHealthIndicator : HealthIndicator {
    override fun health(): Health = Health.up().withDetail("a1", "x1").build()
}
