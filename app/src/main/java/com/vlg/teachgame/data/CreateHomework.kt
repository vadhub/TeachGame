
package com.vlg.teachgame.data

data class CreatedHomework(
    val question: String,
    val options: List<String>,
    val correctIndices: List<Int>,
    val imageUri: String? = null
)