package com.vlg.teachgame.data

data class Question(
    val text: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)
