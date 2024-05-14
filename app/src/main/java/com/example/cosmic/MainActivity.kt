package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmic.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var name: String
    private lateinit var cardNumber: String
    private lateinit var cardExpiryDate: String
    private lateinit var cardCvv: String
    private lateinit var upiId: String
    private lateinit var bal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.qRBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, QRcode::class.java))
            finish()
        }

        binding.profileRedirect.setOnClickListener{
            startActivity(Intent(this@MainActivity, Profile::class.java))
            finish()
        }

        val userId = auth.currentUser?.uid
        val ref = db.collection("Users").document(userId.toString())

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                name = documentSnapshot.get("Name").toString()
                cardNumber = documentSnapshot.get("CardNumber").toString()
                cardExpiryDate = documentSnapshot.get("CardExpiryDate").toString()
                cardCvv = documentSnapshot.get("CardCvv").toString()
                upiId = documentSnapshot.get("UPI_ID").toString()
                bal = documentSnapshot.get("AccountBalance").toString()

                runOnUiThread {
                    binding.cardCvv1.text = cardCvv
                    binding.cardExpDate.text = cardExpiryDate
                    binding.cardNumber.text = cardNumber
                    binding.holderName.text = name
                    binding.upi.text = upiId
                    binding.balance.text = bal
                }
            }
        }

        binding.payMobileIv.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchAcc::class.java))
            finish()
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.bottom_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true
                R.id.bottom_payments -> {
                    startActivity(Intent(applicationContext, Payments::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
                R.id.bottom_scanner -> {
                    startActivity(Intent(applicationContext, Scan::class.java))
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                    true
                }
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
}