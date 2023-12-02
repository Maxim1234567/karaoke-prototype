package ru.araok.entites

interface MediaSubtitle {
    val id: Long?
    val content: Content
    val language: Language
    val subtitles: List<Subtitle>
}