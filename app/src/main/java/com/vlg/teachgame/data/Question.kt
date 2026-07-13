package com.vlg.teachgame.data

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("question") val text: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)
