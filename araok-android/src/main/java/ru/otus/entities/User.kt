package ru.araok.entites

import java.time.LocalDate

interface User {
    val id: Long
    val name: String
    val phone: String
    val password: String
    val birthDate: LocalDate
    val role: String
}