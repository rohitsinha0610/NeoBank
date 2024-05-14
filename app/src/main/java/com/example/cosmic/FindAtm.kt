package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cosmic.databinding.ActivityFindAtmBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class FindAtm : AppCompatActivity() {

    private lateinit var binding: ActivityFindAtmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFindAtmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.exploreBtn.setOnClickListener {
            startActivity(Intent(this@FindAtm, GMap::class.java))
        }



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.selectedItemId = R.id.bottom_find_atm
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

                R.id.bottom_find_atm -> true

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