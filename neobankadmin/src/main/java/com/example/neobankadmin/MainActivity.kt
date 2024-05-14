package com.example.neobankadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neobankadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchUsers::class.java))
            finish()
        }

        binding.allUsers.setOnClickListener {
            startActivity(Intent(this@MainActivity, AllUsers::class.java))
            finish()
        }
    }
}