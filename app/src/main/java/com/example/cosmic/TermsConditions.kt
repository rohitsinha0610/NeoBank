package com.example.cosmic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.cosmic.databinding.ActivitySignupSlidesBinding
import com.example.cosmic.databinding.ActivityTermsConditionsBinding

class TermsConditions : AppCompatActivity() {

    private lateinit var binding: ActivityTermsConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewArea.webViewClient = WebViewClient()
        binding.webViewArea.loadUrl("https://neo-bank.com/terms-condition.html")
        binding.webViewArea.settings.javaScriptEnabled = true
        binding.webViewArea.settings.setSupportZoom(true)
    }
}