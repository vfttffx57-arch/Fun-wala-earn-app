package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Transaction
import com.example.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _leaderboard = MutableStateFlow<List<User>>(emptyList())
    val leaderboard: StateFlow<List<User>> = _leaderboard.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        fetchUserData()
        fetchLeaderboard()
        fetchTransactions()
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.e("UserVM", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.data
                val email = data?.get("email") as? String ?: ""
                val isAdminFromDb = data?.get("isAdmin") as? Boolean ?: false
                val isEmailAdmin = email == "chamanprajapati328@gmail.com"
                
                if (isEmailAdmin && !isAdminFromDb) {
                    db.collection("users").document(uid).update("isAdmin", true)
                }

                _user.value = User(
                    uid = snapshot.id,
                    username = data?.get("username") as? String ?: "",
                    email = email,
                    points = (data?.get("points") as? Number)?.toInt() ?: 0,
                    referralCode = data?.get("referralCode") as? String ?: "",
                    lastCheckIn = data?.get("lastCheckIn") as? Long ?: 0L,
                    isAdmin = isAdminFromDb || isEmailAdmin
                )
            }
        }
    }

    fun fetchLeaderboard() {
        db.collection("users")
            .orderBy("points", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("UserVM", "Leaderboard Listen failed.", e)
                    return@addSnapshotListener
                }
                val list = mutableListOf<User>()
                snapshot?.documents?.forEach { doc ->
                    val data = doc.data
                    list.add(
                        User(
                            uid = doc.id,
                            username = data?.get("username") as? String ?: "Unknown",
                            points = (data?.get("points") as? Number)?.toInt() ?: 0
                        )
                    )
                }
                _leaderboard.value = list
            }
    }

    fun fetchTransactions() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).collection("transactions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                val list = mutableListOf<Transaction>()
                snapshot?.documents?.forEach { doc ->
                    val data = doc.data
                    list.add(
                        Transaction(
                            id = doc.id,
                            title = data?.get("title") as? String ?: "",
                            amount = (data?.get("amount") as? Number)?.toInt() ?: 0,
                            timestamp = data?.get("timestamp") as? Long ?: 0L
                        )
                    )
                }
                _transactions.value = list
            }
    }

    fun addPoints(amount: Int, reason: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                db.runTransaction { transaction ->
                    val userRef = db.collection("users").document(uid)
                    val snapshot = transaction.get(userRef)
                    val currentPoints = (snapshot.get("points") as? Number)?.toInt() ?: 0
                    transaction.update(userRef, "points", currentPoints + amount)
                    
                    val transRef = db.collection("users").document(uid).collection("transactions").document()
                    transaction.set(transRef, hashMapOf(
                        "title" to reason,
                        "amount" to amount,
                        "timestamp" to System.currentTimeMillis()
                    ))
                }.await()
                _message.value = "Earned $amount points!"
            } catch (e: Exception) {
                _message.value = "Failed to add points"
            }
        }
    }

    fun checkIn() {
        val uid = auth.currentUser?.uid ?: return
        val currentUser = _user.value ?: return
        val today = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastCheckInDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(currentUser.lastCheckIn))
        
        if (today == lastCheckInDate) {
            _message.value = "Already checked in today!"
            return
        }

        viewModelScope.launch {
            try {
                // Simplified check-in logic
                db.collection("users").document(uid)
                    .update("lastCheckIn", System.currentTimeMillis())
                    .await()
                addPoints(5, "Daily Check-in")
            } catch (e: Exception) {
                _message.value = "Check-in failed"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
