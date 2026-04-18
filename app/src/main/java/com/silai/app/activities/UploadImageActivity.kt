package com.silai.app.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.silai.app.databinding.ActivityUploadImageBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * UploadImageActivity.kt
 * =======================
 * Allows user to upload a cloth/design reference image using:
 * 1. Camera (takes a new photo)
 * 2. Gallery (picks from existing photos)
 *
 * VIVA TIP:
 * - Camera Intent: ACTION_IMAGE_CAPTURE — opens camera app
 * - Gallery Intent: ACTION_PICK — opens gallery to pick image
 * - FileProvider: required on Android 7+ to share file URIs safely
 * - Runtime Permissions: on Android 6+, we must ask permissions at runtime
 *   using ActivityCompat.requestPermissions()
 * - onActivityResult(): receives the result from camera/gallery intents
 */
class UploadImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadImageBinding
    private var photoUri: Uri? = null
    private val TAG = "UploadImageActivity"

    companion object {
        private const val REQUEST_CAMERA = 100
        private const val REQUEST_GALLERY = 101
        private const val REQUEST_CAMERA_PERMISSION = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnChooseImage.setOnClickListener {
            showImagePickerDialog()
        }

        binding.btnSaveImage.setOnClickListener {
            if (photoUri != null) {
                Toast.makeText(this, "✅ Image saved successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Show dialog to pick Camera or Gallery
    private fun showImagePickerDialog() {
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(arrayOf("📷 Camera", "🖼️ Gallery")) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> openGallery()
                }
            }
            .show()
    }

    // Check camera permission before opening camera
    private fun checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted — request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }
    }

    // Open camera intent
    private fun openCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "com.silai.app.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    // Open gallery intent
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_GALLERY)
    }

    // Create a unique image file in external storage
    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("CLOTH_${timestamp}_", ".jpg", storageDir)
    }

    // Handle result from camera/gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA -> {
                    // Camera saves image to photoUri
                    Glide.with(this).load(photoUri).into(binding.ivPreview)
                    binding.tvImageStatus.text = "✅ Photo captured successfully"
                }
                REQUEST_GALLERY -> {
                    photoUri = data?.data
                    Glide.with(this).load(photoUri).into(binding.ivPreview)
                    binding.tvImageStatus.text = "✅ Image selected from gallery"
                }
            }
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}