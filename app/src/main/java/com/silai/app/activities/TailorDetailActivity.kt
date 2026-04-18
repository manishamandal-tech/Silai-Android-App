package com.silai.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.silai.app.R
import com.silai.app.database.DatabaseHelper
import com.silai.app.databinding.ActivityTailorDetailBinding
import com.silai.app.models.Tailor

/**
 * TailorDetailActivity.kt
 * ========================
 * Displays detailed info about a selected tailor.
 *
 * Receives: TAILOR_ID via Intent extras
 * Shows: name, rating, area, address, description, image
 * Button: "Book Now" → goes to BookingActivity
 *
 * VIVA TIP:
 * - Intent Extras: we pass data between activities using putExtra() / getIntExtra()
 * - Glide: third-party image loading library. Handles URL→ImageView loading, caching, and error images.
 *   Usage: Glide.with(context).load(url).into(imageView)
 */
class TailorDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTailorDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private var tailorId: Int = -1
    private var currentTailor: Tailor? = null
    private val TAG = "TailorDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityTailorDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Retrieve the tailor ID passed from HomeActivity
        tailorId = intent.getIntExtra("TAILOR_ID", -1)
        Log.d(TAG, "Received TAILOR_ID: $tailorId")

        if (tailorId == -1) {
            Toast.makeText(this, "Error loading tailor", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadTailorDetails()
        setupButtons()

        // Back button
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun loadTailorDetails() {
        currentTailor = dbHelper.getTailorById(tailorId)
        currentTailor?.let { tailor ->
            binding.tvTailorName.text = tailor.name
            binding.tvArea.text = "📍 ${tailor.area}"
            binding.tvAddress.text = tailor.address
            binding.tvDescription.text = tailor.description
            binding.tvPhone.text = "📞 ${tailor.phone}"
            binding.ratingBar.rating = tailor.rating.toFloat()
            binding.tvRatingValue.text = "(${tailor.rating})"

            // Load image using Glide
            Glide.with(this)
                .load(tailor.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_tailor_placeholder)
                .error(R.drawable.ic_tailor_placeholder)
                .into(binding.ivTailorMain)

            // Load sample clothing images (using predefined image URLs)
            loadClothingImages()

        } ?: run {
            Toast.makeText(this, "Tailor not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadClothingImages() {
        // Sample clothing images from Unsplash (free-to-use)
        val clothingImages = listOf(
            "https://images.unsplash.com/photo-1585487000160-6ebcfceb0d03?w=300", // Kurta
            "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=300", // Blouse
            "https://images.unsplash.com/photo-1594938298603-c8148c4beed0?w=300", // Shirt
            "https://images.unsplash.com/photo-1553659971-f01207815431?w=300"  // Dress
        )

        Glide.with(this).load(clothingImages[0]).placeholder(R.drawable.ic_tailor_placeholder)
            .error(R.drawable.ic_tailor_placeholder).into(binding.ivCloth1)
        Glide.with(this).load(clothingImages[1]).placeholder(R.drawable.ic_tailor_placeholder)
            .error(R.drawable.ic_tailor_placeholder).into(binding.ivCloth2)
        Glide.with(this).load(clothingImages[2]).placeholder(R.drawable.ic_tailor_placeholder)
            .error(R.drawable.ic_tailor_placeholder).into(binding.ivCloth3)
        Glide.with(this).load(clothingImages[3]).placeholder(R.drawable.ic_tailor_placeholder)
            .error(R.drawable.ic_tailor_placeholder).into(binding.ivCloth4)
    }

    private fun setupButtons() {
        binding.btnBook.setOnClickListener {
            currentTailor?.let { tailor ->
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra("TAILOR_ID", tailor.id)
                intent.putExtra("TAILOR_NAME", tailor.name)
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}