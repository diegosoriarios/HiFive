package com.diego.hifive.models

import com.google.gson.Gson

data class Message(
    val text: String = "",
    val sender: String = ""
) {
    fun toJSON() : String {
        return Gson().toJson(this)
    }

    fun fromJSON(value: String) : Message {
        return Gson().fromJson(value, Message::class.java)
    }
}