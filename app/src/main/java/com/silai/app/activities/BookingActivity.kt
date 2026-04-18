package com.silai.app.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.silai.app.database.DatabaseHelper
import com.silai.app.database.NotificationHelper
import com.silai.app.database.SessionManager
import com.silai.app.databinding.ActivityBookingBinding
import java.util.Calendar

/**
 * BookingActivity.kt
 * ====================
 * Allows user to book a tailor for home pickup/delivery.
 *
 * Features:
 * - Spinner for cloth type (Kurta / Blouse / Shirt / Suit / Saree / Other)
 * - DatePickerDialog for selecting date
 * - TimePickerDialog for selecting time
 * - EditText for special instructions
 * - AlertDialog confirmation before placing order
 * - Notification shown after booking
 *
 * VIVA TIP:
 * - DatePickerDialog: built-in Android dialog for picking dates
 * - TimePickerDialog: built-in Android dialog for picking time
 * - AlertDialog: popup dialog with confirm/cancel buttons
 *   Builder pattern: AlertDialog.Builder(context).setTitle().setMessage().setPositiveButton().show()
 */
class BookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookingBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private var tailorId: Int = -1
    private var tailorName: String = ""
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private val TAG = "BookingActivity"

    private val clothTypes = listOf("Kurta", "Blouse", "Shirt", "Salwar Suit", "Saree Blouse", "Lehenga", "Other")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Get tailor info from intent
        tailorId = intent.getIntExtra("TAILOR_ID", -1)
        tailorName = intent.getStringExtra("TAILOR_NAME") ?: "Tailor"

        binding.tvTailorName.text = "Booking with: $tailorName"

        setupClothSpinner()
        setupDateTimePickers()
        setupBookButton()

        binding.btnBack.setOnClickListener { finish() }

        // Upload image button
        binding.btnUploadImage.setOnClickListener {
            startActivity(Intent(this, UploadImageActivity::class.java))
        }
    }

    private fun setupClothSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clothTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerClothType.adapter = adapter
    }

    private fun setupDateTimePickers() {
        // DATE PICKER — opens calendar dialog
        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, y, m, d ->
                selectedDate = "$d/${m + 1}/$y"
                binding.tvSelectedDate.text = "📅 $selectedDate"
                Log.d(TAG, "Date selected: $selectedDate")
            }, year, month, day).show()
        }

        // TIME PICKER — opens time dialog
        binding.btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, h, m ->
                val amPm = if (h < 12) "AM" else "PM"
                val hour12 = if (h == 0) 12 else if (h > 12) h - 12 else h
                selectedTime = String.format("%d:%02d %s", hour12, m, amPm)
                binding.tvSelectedTime.text = "🕐 $selectedTime"
                Log.d(TAG, "Time selected: $selectedTime")
            }, hour, minute, false).show()
        }
    }

    private fun setupBookButton() {
        binding.btnConfirmBooking.setOnClickListener {
            val clothType = clothTypes[binding.spinnerClothType.selectedItemPosition]
            val instructions = binding.etInstructions.text.toString().trim()

            // Validation
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a pickup date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a pickup time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show AlertDialog confirmation
            AlertDialog.Builder(this)
                .setTitle("✅ Confirm Booking")
                .setMessage(
                    "Tailor: $tailorName\n" +
                            "Cloth Type: $clothType\n" +
                            "Pickup Date: $selectedDate\n" +
                            "Pickup Time: $selectedTime\n\n" +
                            "Instructions: ${if (instructions.isEmpty()) "None" else instructions}\n\n" +
                            "Confirm your booking?"
                )
                .setPositiveButton("Book Now") { _, _ ->
                    placeOrder(clothType, instructions)
                }
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show()
        }
    }

    private fun placeOrder(clothType: String, instructions: String) {
        val userId = sessionManager.getUserId()
        val orderId = dbHelper.placeOrder(
            userId, tailorId, tailorName, clothType,
            selectedDate, selectedTime, instructions, ""
        )

        if (orderId > 0) {
            Log.d(TAG, "Order placed: ID=$orderId")

            // Show notification
            NotificationHelper.showOrderNotification(
                this,
                "🎉 Booking Confirmed!",
                "Your $clothType has been booked with $tailorName. Pickup on $selectedDate",
                orderId.toInt()
            )

            Toast.makeText(this, "Booking confirmed! Order #$orderId", Toast.LENGTH_LONG).show()

            // Navigate to Order Tracking
            val intent = Intent(this, OrderTrackingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}