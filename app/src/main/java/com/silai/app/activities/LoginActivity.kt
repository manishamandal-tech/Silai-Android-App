package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.DatabaseHelper
import com.silai.app.database.SessionManager
import com.silai.app.databinding.ActivityLoginBinding

/**
 * LoginActivity.kt
 * ==================
 * Handles user login.
 *
 * Flow:
 * 1. User enters username + password
 * 2. We query SQLite via DatabaseHelper.loginUser()
 * 3. If found → save session → go to HomeActivity
 * 4. If not → show Toast error
 *
 * Also has buttons for:
 * - Register (go to RegisterActivity)
 * - Admin Login (go to AdminLoginActivity)
 *
 * VIVA TIP:
 * - Toast: short popup message (Toast.makeText)
 * - Intent: used to navigate between activities (startActivity)
 * - ViewBinding: auto-generated class to access XML views safely (no findViewById)
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Login Button Click
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = dbHelper.loginUser(username, password)
            if (user != null) {
                // Save session
                sessionManager.saveLoginSession(user.id, user.username)
                Log.d(TAG, "Login success: ${user.username}")
                Toast.makeText(this, "Welcome, ${user.username}!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Log.d(TAG, "Login failed for: $username")
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Register Button
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Admin Login Button
        binding.tvAdminLogin.setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}