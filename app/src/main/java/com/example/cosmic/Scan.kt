package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.cosmic.databinding.ActivityScanBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Scan : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var userId: String
    private lateinit var otherUserId: String
    private lateinit var otherUserUpiId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser?.uid.toString()

        codeScanner = CodeScanner(this, binding.scanner)
        activityResultLauncher.launch(arrayOf(Manifest.permission.CAMERA))

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.bottom_scanner
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_payments -> {
                    startActivity(Intent(applicationContext, Payments::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_scanner -> true

                R.id.bottom_find_atm -> {
                    startActivity(Intent(applicationContext, FindAtm::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_settings -> {
                    startActivity(Intent(applicationContext, Setting::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setUpQrScanner() {
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val data = it.toString()
                otherUserUpiId = data.substring(10)
                MyFunctions.showToast(this@Scan, "Scan result: $data")
                checkUpiId(otherUserUpiId)
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                MyFunctions.showToast(this@Scan, "Error Scanning: ${it.message}")
            }
        }
        codeScanner.startPreview()
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts
        .RequestMultiplePermissions()) { permission ->
            permission.entries.forEach{
                //val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    setUpQrScanner()
                } else {
                    MyFunctions.showToast(this@Scan, "Please enable camera permission.")
                }
            }
        }

    private fun checkUpiId(oUserUpiId: String) {
        val otherUsersCollection = db.collection("Users")
        val query: Query = otherUsersCollection.whereEqualTo("UPI_ID", oUserUpiId)
        query.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                // UPI ID not found
                MyFunctions.showToast(this@Scan, "User not found")
                startActivity(Intent(this@Scan, MainActivity::class.java))
                finish()
                // Handle the case when the UPI ID is not associated with any user.
            } else {
                val document = querySnapshot.documents[0]
                otherUserId = document.getString("UserId").toString()
                if (otherUserId == userId) {
                    MyFunctions.showToast(this@Scan, "You have scanned your own QR code")
                    startActivity(Intent(this@Scan, MainActivity::class.java))
                    finish()
                } else  {
                    val intent = Intent(this@Scan, PayTo::class.java)
                    intent.putExtra("otherUserId", otherUserId)
                    startActivity(intent)
                }
                // User ID associated with the UPI ID //you have scanned your own QR code please scan another qr code to continue
                // You can use userId for further actions or retrieve user details.
            }
        }
            .addOnFailureListener { exception ->
                MyFunctions.showToast(this@Scan, "Database Error")
                startActivity(Intent(this@Scan, MainActivity::class.java))
                finish()

            }

    }


}