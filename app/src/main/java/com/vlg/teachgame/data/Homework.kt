package com.vlg.teachgame.data

data class Homework(
    val text: String,                    // текст вопроса
    val isCorrect: Boolean = false,      // для простых утверждений
    val options: List<String>? = null,   // варианты ответов (если null – это утверждение)
    val correctIndices: List<Int>? = null, // индексы правильных вариантов
    val imageUri: String? = null         // опциональная картинка
)