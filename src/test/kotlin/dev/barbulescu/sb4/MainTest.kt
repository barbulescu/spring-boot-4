package dev.barbulescu.sb4

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class MainTest {

	@Test
	fun contextLoads() {
	}

}
