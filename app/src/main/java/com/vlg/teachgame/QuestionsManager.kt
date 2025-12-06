package com.vlg.teachgame

import com.vlg.teachgame.data.Question

interface QuestionsManager {
    fun get(): List<Question>
}