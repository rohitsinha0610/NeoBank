package com.example.cosmic

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.example.cosmic.databinding.ActivityKyc1Binding
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class KYC1 : AppCompatActivity() {

    private lateinit var binding: ActivityKyc1Binding
    private lateinit var email: String
    private lateinit var name: String
    private lateinit var dob: String
    private lateinit var phoneNo: String
    private lateinit var imageBitmap: Bitmap

    private val cameraPermissionCode = 101
    private val cameraRequestCode = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityKyc1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent?.extras?.getString("userEmail").toString()
        name = intent?.extras?.getString("userName").toString()
        dob = intent?.extras?.getString("userDob").toString()
        phoneNo = intent?.extras?.getString("phoneNo").toString()

        binding.cameraBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    cameraPermissionCode)
            }
        }

    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, cameraRequestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                MyFunctions.showToast(this@KYC1, "Camera permission denied")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == cameraRequestCode && resultCode == RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageViewArea.setImageBitmap(imageBitmap)
            binding.imageViewArea.visibility = View.VISIBLE
            performOCR(imageBitmap)
        }
    }

    private fun performOCR(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = TextRecognizerOptions.Builder()
        val recognizer = TextRecognition.getClient(options.build())
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val extractedText = visionText.text
                searchForText(extractedText)
            }
            .addOnFailureListener { e ->
                MyFunctions.showToast(this, "OCR Failed: ${e.message}")
            }
    }

    private fun searchForText(extractedText: String) {
        val searchTerm = listOf("GOVT. OF INDIA", "INCOME TAX DEPARTMENT", "of India",
            "Government of India", "Permanent", "number card", "Account number card")
        val found = searchTerm.any { term ->
            extractedText.contains(term, ignoreCase = true)
        }
        if (found) {
            MyFunctions.showToast(this@KYC1, "KYC verified Successfully")
            intent = Intent(this@KYC1, AccountCreation::class.java)
            //intent.putExtra("panCard", imageBitmap)
            intent.putExtra("userEmail", email)
            intent.putExtra("userName", name)
            intent.putExtra("userDob", dob)
            intent.putExtra("phoneNo",phoneNo)
            startActivity(intent)
        } else {
            MyFunctions.showToast(this@KYC1,"KYC verification failed, Please Try Again.")
            binding.imageViewArea.setImageDrawable(null)
        }
    }
}
