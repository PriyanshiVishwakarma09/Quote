package com.example.quote.viewModel

import androidx.lifecycle.ViewModel
import com.example.quote.data.SupabaseClient
import io.github.jan.supabase.auth.auth

class SessionViewModel : ViewModel() {

    fun isUserLoggedIn(): Boolean {
        return SupabaseClient.client.auth.currentSessionOrNull() != null
    }
}
