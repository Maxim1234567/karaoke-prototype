package ru.araok.entites

interface UserWithJwtResponse {
    val user: User
    val token: JwtResponse
}