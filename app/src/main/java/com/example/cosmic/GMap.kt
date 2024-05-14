package com.example.cosmic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.cosmic.databinding.ActivityGmapBinding

class GMap : AppCompatActivity() {

    private lateinit var binding: ActivityGmapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGmapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewArea.webViewClient = WebViewClient()
        binding.webViewArea.loadUrl("https://maps.google.com/")
        binding.webViewArea.settings.javaScriptEnabled = true
        binding.webViewArea.settings.setSupportZoom(true)
    }
}