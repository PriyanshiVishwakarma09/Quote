package com.example.quote.utils

import android.content.Context
import com.example.quote.model.Quote
import java.text.SimpleDateFormat
import java.util.*

class DailyQuoteManager(context: Context) {
    private val prefs = context.getSharedPreferences("daily_quote", Context.MODE_PRIVATE)

    fun getDailyQuote(allQuotes: List<Quote>): Quote? {
        if (allQuotes.isEmpty()) return null

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val savedDate = prefs.getString("date", "")
        val savedId = prefs.getString("id", "")

        return if (today == savedDate && savedId!!.isNotEmpty()) {
            allQuotes.find { it.id == savedId } ?: allQuotes.random()
        } else {
            val newQuote = allQuotes.random()
            prefs.edit().putString("date", today).putString("id", newQuote.id).apply()
            newQuote
        }
    }
}