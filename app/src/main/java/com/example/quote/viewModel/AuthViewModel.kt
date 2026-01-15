package com.example.quote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quote.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = SupabaseClient.client.auth

    var isLoading = false
        private set

    var errorMessage: String? = null
        private set

    fun signUp(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    // Add this function to your AuthViewModel class
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signOut()
                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.message
            }
        }
    }
}