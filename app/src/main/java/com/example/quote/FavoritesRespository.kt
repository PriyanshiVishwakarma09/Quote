package com.example.quote

import com.example.quote.data.SupabaseClient
import com.example.quote.model.Favorite
import io.github.jan.supabase.postgrest.postgrest

class FavoritesRepository {

    private val db = SupabaseClient.client.postgrest

    suspend fun addFavorite(favorite: Favorite) {
        db["favorites"].insert(favorite)
    }

    suspend fun removeFavorite(quoteId: String, userId: String) {
        db["favorites"].delete {
            // ERROR WAS HERE: You missed the 'filter' block
            filter {
                eq("quote_id", quoteId)
                eq("user_id", userId)
            }
        }
    }

    suspend fun getFavorites(userId: String): List<Favorite> {
        return db["favorites"]
            .select {
                // ERROR WAS HERE: You missed the 'filter' block
                filter {
                    eq("user_id", userId)
                }
            }
            .decodeList()
    }
}
