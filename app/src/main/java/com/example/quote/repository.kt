package com.example.quote

import com.example.quote.data.SupabaseClient
import com.example.quote.model.Quote
import io.github.jan.supabase.postgrest.from


class QuoteRepository {

    suspend fun getQuotes(): List<Quote> {
        return SupabaseClient.client
            .from("quotes")
            .select()
            .decodeList<Quote>()
    }

    suspend fun getMotivationQuotes(): List<Quote> {
        return SupabaseClient.client
            .from("quotes")
            .select {
                filter {
                    eq("category", "Motivation")
                }
            }
            .decodeList<Quote>()
    }
}