package ru.araok.entites

interface Subtitle {
    val id: Long?
    val line: String
    val to: Long
    val from: Long
}