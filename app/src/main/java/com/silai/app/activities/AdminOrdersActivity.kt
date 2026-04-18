package com.silai.app.activities
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.silai.app.adapters.AdminOrderAdapter
import com.silai.app.database.DatabaseHelper
import com.silai.app.database.NotificationHelper
import com.silai.app.databinding.ActivityAdminOrdersBinding

class AdminOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminOrdersBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: AdminOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = DatabaseHelper(this)
        binding.btnBack.setOnClickListener { finish() }
        adapter = AdminOrderAdapter(mutableListOf()) { order ->
            // Show status update dialog
            val statuses = arrayOf("Pickup Scheduled","Stitching in Progress","Out for Delivery","Delivered")
            AlertDialog.Builder(this)
                .setTitle("Update Order #${order.id} Status")
                .setItems(statuses) { _, which ->
                    dbHelper.updateOrderStatus(order.id, statuses[which])
                    NotificationHelper.showOrderNotification(this,
                        "Order Update", "Your order is now: ${statuses[which]}", order.id)
                    Toast.makeText(this, "Status updated!", Toast.LENGTH_SHORT).show()
                    loadOrders()
                }.show()
        }
        binding.rvAdminOrders.layoutManager = LinearLayoutManager(this)
        binding.rvAdminOrders.adapter = adapter
        loadOrders()
    }

    private fun loadOrders() {
        val orders = dbHelper.getAllOrders()
        if (orders.isEmpty()) { binding.tvNoOrders.visibility = View.VISIBLE }
        else { binding.tvNoOrders.visibility = View.GONE; adapter.updateData(orders) }
    }
}