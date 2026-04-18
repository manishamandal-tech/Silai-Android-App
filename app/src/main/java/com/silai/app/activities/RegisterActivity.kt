package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.DatabaseHelper
import com.silai.app.databinding.ActivityRegisterBinding

/**
 * RegisterActivity.kt
 * =====================
 * Handles new user registration.
 *
 * Flow:
 * 1. Validate all fields (not empty, passwords match)
 * 2. Check username not already taken
 * 3. Insert into SQLite → go back to LoginActivity
 *
 * VIVA TIP:
 * - We use dbHelper.isUsernameTaken() to check uniqueness BEFORE inserting
 * - registerUser() returns the new row's ID (-1 if failed)
 * - finish() pops this activity off the back stack
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPass = binding.etConfirmPassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            // Validation
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPass) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 4) {
                Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dbHelper.isUsernameTaken(username)) {
                Toast.makeText(this, "Username already taken, try another", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.registerUser(username, password, email, phone)
            if (result > 0) {
                Log.d(TAG, "Registration successful: $username")
                Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvLogin.setOnClickListener { finish() }
    }
}