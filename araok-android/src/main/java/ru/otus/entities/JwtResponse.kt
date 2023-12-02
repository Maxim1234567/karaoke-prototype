package ru.araok.entites

interface JwtResponse {
    val accessToken: String?
    val refreshToken: String?
}