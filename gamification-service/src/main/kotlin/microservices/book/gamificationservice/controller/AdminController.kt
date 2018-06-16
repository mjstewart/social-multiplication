package microservices.book.gamificationservice.controller

import microservices.book.gamificationservice.service.AdminService
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Profile("test")
@RestController
@RequestMapping("/gamification/admin")
class AdminController(private val adminService: AdminService) {

    @PostMapping("/delete-db")
    fun deleteDatabase(): ResponseEntity<Any> {
        adminService.deleteDatabaseContents()
        return ResponseEntity.ok().build()
    }
}