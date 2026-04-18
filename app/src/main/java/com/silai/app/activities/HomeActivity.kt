package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.silai.app.adapters.TailorAdapter
import com.silai.app.database.DatabaseHelper
import com.silai.app.database.SessionManager
import com.silai.app.databinding.ActivityHomeBinding
import com.silai.app.models.Tailor

/**
 * HomeActivity.kt
 * =================
 * Main screen after login.
 *
 * Features:
 * - Spinner (dropdown) to select area
 * - RecyclerView showing tailors in that area
 * - Toolbar with logout & my orders options
 *
 * VIVA TIP:
 * - Spinner: dropdown widget, uses ArrayAdapter to populate items
 * - RecyclerView: scrollable list, requires Adapter + LayoutManager
 * - LinearLayoutManager: arranges items in a vertical/horizontal list
 * - AdapterView.OnItemSelectedListener: callback when spinner item changes
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var tailorAdapter: TailorAdapter
    private val TAG = "HomeActivity"

    // Areas available in the Spinner
    private val areas = listOf(
        "Select Area", "Dombivli", "Thane", "Kalyan",
        "Navi Mumbai", "Mumbai", "Pune", "Nashik"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Set welcome message
        binding.tvWelcome.text = "Hello, ${sessionManager.getUsername()} 👋"

        setupSpinner()
        setupRecyclerView()
        setupButtons()
    }

    private fun setupSpinner() {
        // ArrayAdapter connects the list of areas to the Spinner widget
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, areas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerArea.adapter = adapter

        // Callback when user selects a different area
        binding.spinnerArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedArea = areas[position]
                Log.d(TAG, "Area selected: $selectedArea")
                if (position == 0) {
                    // Show ALL tailors when no area selected
                    loadAllTailors()
                } else {
                    loadTailorsByArea(selectedArea)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupRecyclerView() {
        tailorAdapter = TailorAdapter(mutableListOf()) { tailor ->
            // On item click → open TailorDetailActivity with tailor ID
            val intent = Intent(this, TailorDetailActivity::class.java)
            intent.putExtra("TAILOR_ID", tailor.id)
            startActivity(intent)
        }
        // LinearLayoutManager = vertical scrolling list
        binding.rvTailors.layoutManager = LinearLayoutManager(this)
        binding.rvTailors.adapter = tailorAdapter

        loadAllTailors()
    }

    private fun loadAllTailors() {
        val tailors = dbHelper.getAllTailors()
        updateTailorList(tailors)
    }

    private fun loadTailorsByArea(area: String) {
        val tailors = dbHelper.getTailorsByArea(area)
        if (tailors.isEmpty()) {
            binding.tvNoTailors.visibility = View.VISIBLE
            binding.rvTailors.visibility = View.GONE
        } else {
            binding.tvNoTailors.visibility = View.GONE
            binding.rvTailors.visibility = View.VISIBLE
        }
        updateTailorList(tailors)
    }

    private fun updateTailorList(tailors: List<Tailor>) {
        tailorAdapter.updateData(tailors)
        binding.tvTailorCount.text = "${tailors.size} tailors found"
    }

    private fun setupButtons() {
        // My Orders button
        binding.btnMyOrders.setOnClickListener {
            startActivity(Intent(this, OrderTrackingActivity::class.java))
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    sessionManager.logout()
                    Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // Refresh the list when coming back from booking
        val selectedArea = areas[binding.spinnerArea.selectedItemPosition]
        if (binding.spinnerArea.selectedItemPosition == 0) loadAllTailors()
        else loadTailorsByArea(selectedArea)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}