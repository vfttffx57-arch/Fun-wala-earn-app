package com.example

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds
import android.util.Log

class FunEarnApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase with the provided keys
        val options = FirebaseOptions.Builder()
            .setApiKey("AIzaSyANpT9kPU3GPGo_RrHf3vrLsaHX2Q9fmwk")
            .setApplicationId("1:1009501510035:web:5dea280689c59f97947066") // Note: The Web App ID provided by user
            .setProjectId("fun-earn-50176")
            .setStorageBucket("fun-earn-50176.firebasestorage.app")
            .build()
            
        try {
            FirebaseApp.initializeApp(this, options)
            Log.d("FunEarn", "Firebase Initialized successfully")
        } catch (e: Exception) {
            Log.e("FunEarn", "Error initializing Firebase: ${e.message}")
        }

        // Initialize Unity Ads
        val unityGameId = "6010039"
        val testMode = false
        UnityAds.initialize(this, unityGameId, testMode, object : IUnityAdsInitializationListener {
            override fun onInitializationComplete() {
                Log.d("FunEarn", "Unity Ads Initialized successfully")
            }
            override fun onInitializationFailed(error: UnityAds.UnityAdsInitializationError?, message: String?) {
                Log.e("FunEarn", "Unity Ads initialization failed: $message")
            }
        })
    }
}
