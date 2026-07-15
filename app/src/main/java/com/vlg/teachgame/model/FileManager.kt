package com.vlg.teachgame.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vlg.teachgame.data.CreatedHomework
import com.vlg.teachgame.data.Homework
import com.vlg.teachgame.data.Question
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class FileManager {

    fun convertInputStreamToString(inputStream: InputStream): String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            stringBuilder.append(line)
        }
        inputStream.close()
        return stringBuilder.toString()
    }

    fun parseQuestions(jsonString: String): List<Question> {
        val gson = Gson()
        val type = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(jsonString, type)
    }

    fun parseQuestions(inputStream: InputStream) =
        parseQuestions(convertInputStreamToString(inputStream))

    fun parseHomeworks(json: String): List<Homework> {
        if (json.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<Homework>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }

    fun saveCreatedHomeworks(createdHomeworks: List<CreatedHomework>): String {
        val gson = Gson()
        val json = gson.toJson(createdHomeworks)
        return json
    }

    fun loadCreatedHomeworks(json: String): List<CreatedHomework>  {
        if (json.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<CreatedHomework>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }

    fun parseHomeworks(inputStream: InputStream) =
        parseHomeworks(convertInputStreamToString(inputStream))

}