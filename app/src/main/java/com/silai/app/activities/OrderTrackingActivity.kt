package com.silai.app.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.silai.app.adapters.OrderAdapter
import com.silai.app.database.DatabaseHelper
import com.silai.app.database.SessionManager
import com.silai.app.databinding.ActivityOrderTrackingBinding

/**
 * OrderTrackingActivity.kt
 * ==========================
 * Shows all orders for the logged-in user with their current status.
 *
 * Status steps:
 * 1. Pickup Scheduled  → 🟡
 * 2. Stitching in Progress → 🔵
 * 3. Out for Delivery → 🟠
 * 4. Delivered → 🟢
 *
 * VIVA TIP:
 * - We use a RecyclerView with OrderAdapter to display each order as a card.
 * - Each card shows progress steps with color coding.
 * - We only show orders belonging to the currently logged-in user (by user_id).
 */
class OrderTrackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderTrackingBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var orderAdapter: OrderAdapter
    private val TAG = "OrderTrackingActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityOrderTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        binding.btnBack.setOnClickListener { finish() }

        setupRecyclerView()
        loadOrders()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(mutableListOf())
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = orderAdapter
    }

    private fun loadOrders() {
        val userId = sessionManager.getUserId()
        val orders = dbHelper.getOrdersByUser(userId)
        Log.d(TAG, "Loaded ${orders.size} orders for user $userId")

        if (orders.isEmpty()) {
            binding.tvNoOrders.visibility = View.VISIBLE
            binding.rvOrders.visibility = View.GONE
        } else {
            binding.tvNoOrders.visibility = View.GONE
            binding.rvOrders.visibility = View.VISIBLE
            orderAdapter.updateData(orders)
        }
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }
}