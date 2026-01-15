package com.example.quote.data


import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://llkngnpxiypyhgrusscb.supabase.co"


    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imxsa25nbnB4aXlweWhncnVzc2NiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjgzNzU5NzMsImV4cCI6MjA4Mzk1MTk3M30.gqfCDbIRONioRX36b0pfttdtuKmclHeGfdgUf_YMUXY"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}