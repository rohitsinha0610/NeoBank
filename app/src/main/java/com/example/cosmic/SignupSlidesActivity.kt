package com.example.cosmic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cosmic.databinding.ActivitySignupSlidesBinding

class SignupSlidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupSlidesBinding
    private lateinit var imageSlider: ImageSlider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupSlidesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageSlider = findViewById(R.id.signupImageSlider)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.logo_color))
        imageList.add(SlideModel(R.drawable.neo_logo))
        imageList.add(SlideModel(R.drawable.sinlogbg))

        imageSlider.setImageList(imageList, ScaleTypes.CENTER_INSIDE)

        binding.checkBox.setOnClickListener {
            val isChecked = binding.checkBox.isChecked
            binding.signupSlideBtn.isEnabled = isChecked
            binding.signupSlideBtn.visibility = View.VISIBLE
        }

        binding.term.setOnClickListener {
            startActivity(Intent(this@SignupSlidesActivity, TermsConditions::class.java))
        }

        binding.signupSlideBtn.setOnClickListener {
            if (binding.signupSlideBtn.isEnabled){
                startActivity(Intent(this@SignupSlidesActivity, SignUp::class.java))
                finish()
            }else{
                MyFunctions.showToast(this@SignupSlidesActivity, "Please Check Terms and Conditions")
            }
        }

        binding.loginRedirect.setOnClickListener {
            startActivity(Intent(this@SignupSlidesActivity, Login::class.java))
            finish()
        }
    }
}