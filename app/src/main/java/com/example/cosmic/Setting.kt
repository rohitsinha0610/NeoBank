package com.example.cosmic

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.cosmic.databinding.ActivitySettingBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Setting : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var name: String
    private lateinit var upiId: String
    private lateinit var accNo: String
    private lateinit var ifsc: String
    private lateinit var accType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.qrBtn.setOnClickListener {
            startActivity(Intent(this@Setting, QRcode::class.java))
            finish()
        }

        binding.saVBtn.setOnClickListener {
            startActivity(Intent(this@Setting, Profile::class.java))
            finish()
        }

        val userId = auth.currentUser?.uid
        val ref = db.collection("Users").document(userId.toString())

        ref.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                name = documentSnapshot.get("Name").toString()
                accNo = documentSnapshot.get("AccountNumber").toString()
                ifsc = documentSnapshot.get("IFSC_Code").toString()
                accType = documentSnapshot.get("AccountType").toString()
                upiId = documentSnapshot.get("UPI_ID").toString()

                runOnUiThread {
                    binding.nametv.text = name
                    binding.accNo.text = accNo
                    binding.ifsc.text = ifsc
                    binding.acTy.text = accType
                    binding.upi.text = upiId
                }
            }
        }

        binding.logoutTextView.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.confirm_logout, null)

            builder.setView(view)
            val dialog = builder.create()

            view.findViewById<Button>(R.id.logoutBtn).setOnClickListener {
                auth.signOut()
                dialog.dismiss()
                startActivity(Intent(this@Setting, Login::class.java))
                finish()
            }
            view.findViewById<Button>(R.id.logCancelBtn).setOnClickListener {
                //pending
                dialog.dismiss()
            }
            if (dialog.window != null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.bottom_settings
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

                R.id.bottom_settings -> true

                else -> false
            }
        }
    }
}