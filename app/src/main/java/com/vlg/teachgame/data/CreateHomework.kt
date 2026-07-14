
package com.vlg.teachgame.data

data class CreatedHomework(
    val question: String,
    val correctAnswer: String,
    val imageUri: String? = null
)