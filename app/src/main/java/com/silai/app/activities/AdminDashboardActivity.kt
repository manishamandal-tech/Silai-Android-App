package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.DatabaseHelper
import com.silai.app.databinding.ActivityAdminDashboardBinding

/**
 * AdminDashboardActivity.kt
 * ==========================
 * Admin control panel showing summary stats and navigation.
 *
 * Shows:
 * - Total tailors count
 * - Total orders count
 * - Buttons to: Add Tailor, View All Orders
 *
 * VIVA TIP: This is a simple dashboard pattern — fetch counts and show buttons.
 * In a real app, this would have charts and live stats.
 */
class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminDashboardBinding
    private lateinit var dbHelper: DatabaseHelper
    private val TAG = "AdminDashboardActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        binding.btnAddTailor.setOnClickListener {
            startActivity(Intent(this, AddTailorActivity::class.java))
        }

        binding.btnViewOrders.setOnClickListener {
            startActivity(Intent(this, AdminOrdersActivity::class.java))
        }

        binding.btnLogoutAdmin.setOnClickListener {
            finish()
        }

        loadStats()
    }

    private fun loadStats() {
        val tailorCount = dbHelper.getAllTailors().size
        val orderCount = dbHelper.getAllOrders().size
        binding.tvTailorCount.text = "Total Tailors: $tailorCount"
        binding.tvOrderCount.text = "Total Orders: $orderCount"
    }

    override fun onResume() {
        super.onResume()
        loadStats()
    }
}