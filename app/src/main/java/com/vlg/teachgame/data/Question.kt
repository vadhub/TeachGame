package com.vlg.teachgame.data

data class Question(
    val text: String,
    val answerTrue: String,
    val variants: List<String>
)
