package dev.barbulescu.sb4

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestConstructor(autowireMode = ALL)
@AutoConfigureWebTestClient
annotation class IntegrationTest()
