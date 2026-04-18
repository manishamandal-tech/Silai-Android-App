package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.NotificationHelper
import com.silai.app.database.SessionManager
import com.silai.app.databinding.ActivitySplashBinding

/**
 * SplashActivity.kt
 * ===================
 * The first screen shown when app launches (declared as LAUNCHER in Manifest).
 * Shows app logo/branding for 2 seconds, then navigates to:
 *   - HomeActivity if already logged in (session saved)
 *   - LoginActivity if not logged in
 *
 * VIVA TIP: Activity Lifecycle:
 *   onCreate → onStart → onResume → (app running) → onPause → onStop → onDestroy
 *   All are overridden here with Log.d() calls for demonstration.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Splash screen started")

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Create notification channel once
        NotificationHelper.createNotificationChannel(this)

        // Wait 2 seconds, then navigate
        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.isLoggedIn()) {
                Log.d(TAG, "User already logged in → HomeActivity")
                startActivity(Intent(this, HomeActivity::class.java))
            } else {
                Log.d(TAG, "Not logged in → LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()  // Remove SplashActivity from back stack
        }, 2000)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Splash visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Splash interactive")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Splash losing focus")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Splash hidden")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Splash destroyed")
    }
}