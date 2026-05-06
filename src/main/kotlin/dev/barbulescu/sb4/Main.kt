package dev.barbulescu.sb4

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}

@Configuration
class ApplicationConfig {
//    @Bean
//    fun keyPair(): KeyPairGenerator = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }
//
//    @Bean
//    fun jwtEncoder(keyPair: KeyPair): NimbusJwtEncoder {
//        val rsaKey = RSAKey.Builder(keyPair.public as RSAPublicKey)
//            .privateKey(keyPair.private as RSAPrivateKey)
//            .keyID("test-key-id")
//            .build()
//        val jwkSet = JWKSet(rsaKey)
//        val jwkSource = ImmutableJWKSet<SecurityContext>(jwkSet)
//        return NimbusJwtEncoder(jwkSource)
//    }
//
//    @Bean
//    @Primary
//    fun jwtTestDecoder(keyPair: KeyPair): JwtDecoder = NimbusJwtDecoder
//        .withPublicKey(keyPair.public as RSAPublicKey)
//        .build()
}
