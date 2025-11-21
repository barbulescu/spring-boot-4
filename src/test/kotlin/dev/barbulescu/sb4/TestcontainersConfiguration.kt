package dev.barbulescu.sb4

import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

//	@Bean
//	@ServiceConnection
//	fun grafanaLgtmContainer(): LgtmStackContainer {
//		return LgtmStackContainer(DockerImageName.parse("grafana/otel-lgtm:latest"))
//	}

//	@Bean
//	@ServiceConnection
//	fun postgresContainer(): PostgreSQLContainer {
//		return PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
//	}

}
