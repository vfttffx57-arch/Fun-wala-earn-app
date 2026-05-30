package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        // Check if user is already signed in
        if (auth.currentUser != null) {
            _authState.value = AuthState.Success
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill all fields")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email.trim(), pass).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(username: String, email: String, pass: String, referralCode: String?) {
        if (username.isBlank() || email.isBlank() || pass.isBlank()) {
            _authState.value = AuthState.Error("Please fill required fields")
            return
        }
        if (pass.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters")
            return
        }
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email.trim(), pass).await()
                val uid = result.user?.uid ?: throw Exception("User creation failed")
                
                // create unique referral code
                val userRefCode = username.take(3).uppercase() + (1000..9999).random().toString()
                
                val userMap = hashMapOf(
                    "uid" to uid,
                    "username" to username.trim(),
                    "email" to email.trim(),
                    "points" to 0,
                    "referralCode" to userRefCode,
                    "referredBy" to (referralCode?.trim() ?: ""),
                    "createdAt" to System.currentTimeMillis()
                )
                
                db.collection("users").document(uid).set(userMap).await()
                
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
    
    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
