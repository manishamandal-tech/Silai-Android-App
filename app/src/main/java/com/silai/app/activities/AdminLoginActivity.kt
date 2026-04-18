package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.databinding.ActivityAdminLoginBinding

/**
 * AdminLoginActivity.kt
 * =======================
 * Simple hardcoded admin login screen.
 * Admin credentials: username=admin, password=1234
 *
 * VIVA TIP: For a real app, you'd check admin credentials against a secure backend.
 * Here we use hardcoded values for simplicity. This is called "hardcoded authentication."
 */
class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private val TAG = "AdminLoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdminLogin.setOnClickListener {
            val username = binding.etAdminUsername.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()

            // Hardcoded admin credentials
            if (username == "admin" && password == "1234") {
                Log.d(TAG, "Admin login success")
                Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminDashboardActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid admin credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}