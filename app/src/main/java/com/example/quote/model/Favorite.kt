package com.example.quote.model

import kotlinx.serialization.Serializable

@Serializable
data class Favorite(
    val id: String? = null,
    val user_id: String,
    val quote_id: String
)
