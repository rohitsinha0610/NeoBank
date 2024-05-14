package com.example.cosmic

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.cosmic.databinding.ActivityAccountCreationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.time.Instant
import kotlin.random.Random

class AccountCreation : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivityAccountCreationBinding

    private lateinit var userId: String
    private lateinit var email: String
    private lateinit var phoneNo: String
    private lateinit var name: String
    private lateinit var dob: String
    private lateinit var qRBitmap: Bitmap

    private lateinit var stateAdapter: ArrayAdapter<CharSequence>
    private lateinit var districtAdapter: ArrayAdapter<CharSequence>

    private lateinit var selectedDistrict: String
    private lateinit var selectedState: String

    private lateinit var accountId: String
    private lateinit var accountNumber: String
    private lateinit var upiId: String
    private lateinit var ifscCode: String
    private lateinit var accountType: String
    private var kyc: Boolean = true

    private lateinit var cardId: String
    private lateinit var cardNumber: String
    private var cardCvv: Int = 0
    private var balance: Double = 50.0
    private lateinit var cardExpiryDate: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        email = intent?.extras?.getString("userEmail").toString()
        userId = auth.currentUser!!.uid
        kyc = true
        phoneNo = intent?.extras?.getString("phoneNo").toString()
        name = intent?.extras?.getString("userName").toString()
        dob = intent?.extras?.getString("userDob").toString()
        //intent.getParcelableExtra<Bitmap>("imageBitmap")!!.also { this.imageBitmap = it }


        ifscCode = "NEOB250303"
        accountType = "Saving Account"
        upiId = "$phoneNo@neobrv".substring(3)
        cardExpiryDate = "06/10/2028"

        stateAdapter = ArrayAdapter.createFromResource(this, R.array.array_indian_states,
            R.layout.spinner_layout)

        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.indianState.adapter = stateAdapter

        binding.indianState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                selectedState = binding.indianState.selectedItem.toString()

                val parentID = parent?.id
                if (parentID == R.id.indianState) {
                    when (selectedState) {
                        "Select Your State" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_default_districts, R.layout.spinner_layout)
                        "Andhra Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_andhra_pradesh_districts, R.layout.spinner_layout)
                        "Arunachal Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_arunachal_pradesh_districts, R.layout.spinner_layout)
                        "Assam" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_assam_districts, R.layout.spinner_layout)
                        "Bihar" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_bihar_districts, R.layout.spinner_layout)
                        "Chhattisgarh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_chhattisgarh_districts, R.layout.spinner_layout)
                        "Goa" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_goa_districts, R.layout.spinner_layout)
                        "Gujarat" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_gujarat_districts, R.layout.spinner_layout)
                        "Haryana" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_haryana_districts, R.layout.spinner_layout)
                        "Himachal Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_himachal_pradesh_districts, R.layout.spinner_layout)
                        "Jharkhand" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_jharkhand_districts, R.layout.spinner_layout)
                        "Karnataka" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_karnataka_districts, R.layout.spinner_layout)
                        "Kerala" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_kerala_districts, R.layout.spinner_layout)
                        "Madhya Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_madhya_pradesh_districts, R.layout.spinner_layout)
                        "Maharashtra" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_maharashtra_districts, R.layout.spinner_layout)
                        "Manipur" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_manipur_districts, R.layout.spinner_layout)
                        "Meghalaya" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_meghalaya_districts, R.layout.spinner_layout)
                        "Mizoram" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_mizoram_districts, R.layout.spinner_layout)
                        "Nagaland" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_nagaland_districts, R.layout.spinner_layout)
                        "Odisha" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_odisha_districts, R.layout.spinner_layout)
                        "Punjab" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_punjab_districts, R.layout.spinner_layout)
                        "Rajasthan" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_rajasthan_districts, R.layout.spinner_layout)
                        "Sikkim" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_sikkim_districts, R.layout.spinner_layout)
                        "Tamil Nadu" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_tamil_nadu_districts, R.layout.spinner_layout)
                        "Telangana" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_telangana_districts, R.layout.spinner_layout)
                        "Tripura" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_tripura_districts, R.layout.spinner_layout)
                        "Uttar Pradesh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_uttar_pradesh_districts, R.layout.spinner_layout)
                        "Uttarakhand" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_uttarakhand_districts, R.layout.spinner_layout)
                        "West Bengal" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_west_bengal_districts, R.layout.spinner_layout)
                        "Andaman and Nicobar Islands" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_andaman_nicobar_districts, R.layout.spinner_layout)
                        "Chandigarh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_chandigarh_districts, R.layout.spinner_layout)
                        "Dadra and Nagar Haveli" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_dadra_nagar_haveli_districts, R.layout.spinner_layout)
                        "Daman and Diu" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_daman_diu_districts, R.layout.spinner_layout)
                        "Delhi" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_delhi_districts, R.layout.spinner_layout)
                        "Jammu and Kashmir" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_jammu_kashmir_districts, R.layout.spinner_layout)
                        "Lakshadweep" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_lakshadweep_districts, R.layout.spinner_layout)
                        "Ladakh" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_ladakh_districts, R.layout.spinner_layout)
                        "Puducherry" -> districtAdapter = ArrayAdapter.createFromResource(parent.context,
                            R.array.array_puducherry_districts, R.layout.spinner_layout)
                    }
                }

                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.stateDistrict.adapter = districtAdapter

                binding.stateDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        selectedDistrict = binding.stateDistrict.selectedItem.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.addressSubmitBtn.setOnClickListener {
            if (selectedState == "Select Your State") {
                MyFunctions.showToast(this@AccountCreation,
                    "Please select your state from the list")
                binding.stateTv.error = "State is required!" // To set error on TextView
                binding.stateTv.requestFocus()
            } else if (selectedDistrict == "Select Your District") {
                MyFunctions.showToast(this@AccountCreation,
                    "Please select your district from the list")
                binding.districtTv.error = "District is required!"
                binding.districtTv.requestFocus()
                binding.stateTv.error = null
            } else {
                binding.stateTv.error = null
                binding.districtTv.error = null
                addData()
            }
        }
    }

    private fun addData() {

        accountId = generateAccountID().toString()
        accountNumber = Instant.now().toEpochMilli().toString()

        cardId = generateCardID().toString()
        cardNumber = generateCardNumber()
        cardCvv = generateCvv()

//        qRBitmap = generateQRCode(upiId,260,)
//        val baos = ByteArrayOutputStream()
//        qRBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val imageData = baos.toByteArray() as List<Byte>

        val userDataMap = hashMapOf(
            "UserId" to userId,
            "Name" to name,
            "Email" to email,
            "DateOfBirth" to dob,
            "MobileNo" to phoneNo,

            "State" to selectedState,
            "District" to selectedDistrict,

            "AccountID" to accountId,
            "AccountNumber" to accountNumber,
            "IFSC_Code" to ifscCode,
            "AccountType" to accountType,
            "KYC" to kyc,
            "UPI_ID" to upiId,

            "AccountBalance" to balance,
            //"QRCodepng" to imageData,
            //"PanCardImage" to imageBitmap,

            "CardID" to cardId,
            "CardNumber" to cardNumber,
            "CardCvv" to cardCvv,
            "CardExpiryDate" to cardExpiryDate
        )

        db.collection("Users").document(userId).set(userDataMap)
            .addOnSuccessListener {
                MyFunctions.showToast(this@AccountCreation, "Signup Successful")
                startActivity(Intent(this@AccountCreation, MainActivity::class.java))

            }
            .addOnFailureListener{
                MyFunctions.showToast(this@AccountCreation, "Signup Unsuccessful")
            }
    }

    private fun generateAccountID(): Int {
        val timestamp = System.currentTimeMillis()
        val random = (100 until 1_000).random()
        return (timestamp + random).toInt()
    }

    private fun generateCardID(): Int {
        val generatedIDs = HashSet<Int>()
        while (true) {
            val id = Random.nextInt(1000, 10000) // Generate a random 4-digit number
            if (id !in generatedIDs) {
                generatedIDs.add(id)
                return id
            }
        }
    }

    private fun generateCardNumber(): String {
        val startingDigit = 4000000000000000
        val secondPart = 522010000000000
        val randomPart = Random.nextLong(1_000_000_000, 9_999_999_999)
        val accNum = startingDigit + secondPart + randomPart
        val formattedCardNumber = accNum.toString()
        return formattedCardNumber.chunked(4).joinToString("  ")
    }

    private fun generateCvv(): Int {
        val generatedNumbers = mutableSetOf<Int>()
        while (true) {
            val randomNumber = (100..999).random()
            if (generatedNumbers.add(randomNumber)) {
                return randomNumber
            }
        }
    }

}