package ru.araok.entites

import java.time.LocalDate

interface Content {
    val id: Long?
    val name: String
    val limit: AgeLimit
    val artist: String
    val user: User
    val createDate: LocalDate
    val language: Language
}