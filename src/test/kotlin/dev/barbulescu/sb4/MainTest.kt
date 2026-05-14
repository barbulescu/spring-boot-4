package dev.barbulescu.sb4

import org.junit.jupiter.api.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import kotlin.test.assertEquals

@IntegrationTest
class MainTest(private val jdbc: JdbcTemplate) {

    @Test
    fun contextLoads() {
    }

    @Test
    fun postgresReachable() {
        assertEquals(1, jdbc.queryForObject<Int>("SELECT 1"))
    }
}
