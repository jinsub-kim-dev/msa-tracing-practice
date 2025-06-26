package com.example.user_service

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("id" to id, "name" to "Test User"))
    }
}