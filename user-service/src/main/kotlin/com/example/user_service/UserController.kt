package com.example.user_service

import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/api/user")
class UserController(
    private val webClient: WebClient
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<Map<String, Any>> {
        log.info("사용자 정보 요청 처리: id=$id")

        val fastApiUrl = "http://fastapi:8000/api/profile/$id"

        val profile = webClient.get()
            .uri(fastApiUrl)
            .retrieve()
            .bodyToMono(object : ParameterizedTypeReference<Map<String, Any>>() {})
            .block() ?: emptyMap()

        return ResponseEntity.ok(
            mapOf(
                "id" to id,
                "name" to "Test User",
                "email" to "$id@example.com",
                "profile" to profile
            )
        )
    }
}