package com.silai.app.activities

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.DatabaseHelper
import com.silai.app.databinding.ActivityAddTailorBinding

/**
 * AddTailorActivity.kt
 * =====================
 * Admin can add a new tailor to the database.
 *
 * Fields: Name, Area (Spinner), Rating, Image URL, Address, Description, Phone
 *
 * VIVA TIP: After inserting, the new tailor immediately appears in HomeActivity
 * because we query the DB fresh every time (no caching).
 */
class AddTailorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTailorBinding
    private lateinit var dbHelper: DatabaseHelper
    private val TAG = "AddTailorActivity"

    private val areas = listOf("Dombivli", "Thane", "Kalyan", "Navi Mumbai", "Mumbai", "Pune", "Nashik")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityAddTailorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, areas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerArea.adapter = adapter

        binding.btnSaveTailor.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val area = areas[binding.spinnerArea.selectedItemPosition]
            val ratingStr = binding.etRating.text.toString().trim()
            val imageUrl = binding.etImageUrl.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val desc = binding.etDescription.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()

            if (name.isEmpty() || ratingStr.isEmpty()) {
                Toast.makeText(this, "Name and Rating are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rating = ratingStr.toDoubleOrNull() ?: 4.0
            val result = dbHelper.addTailor(name, area, rating, imageUrl, address, desc, phone)

            if (result > 0) {
                Log.d(TAG, "Tailor added: $name in $area")
                Toast.makeText(this, "Tailor '$name' added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add tailor", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBack.setOnClickListener { finish() }
    }
}