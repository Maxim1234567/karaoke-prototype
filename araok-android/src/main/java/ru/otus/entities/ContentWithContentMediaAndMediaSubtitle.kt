package ru.araok.entites

interface ContentWithContentMediaAndMediaSubtitle {
    val content: Content
    val mediaSubtitle: MediaSubtitle
    val preview: ContentMedia
    val video: ContentMedia
}