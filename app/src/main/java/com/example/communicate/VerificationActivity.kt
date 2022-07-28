package com.example.communicate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_verification.*
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {

    // important stuff initializing for phone authentication
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        auth = FirebaseAuth.getInstance()

        // check the current user whether he logged in or not
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this,ProfileActivity::class.java))
            finish()
        }

        sendOtp.setOnClickListener{
            logIn()
        }

        // now call back functions for Phone auth
        callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                startActivity(Intent(applicationContext,ProfileActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext, "Failed!!!!", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                var intent = Intent(applicationContext ,OtpActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }

        }
    }

    private fun logIn() {

        var number = edtNumber.text.toString()

        if (!number.isEmpty()){

            number= "+91"+number
            sendVerificationCode(number)

        }
        else{
            Toast.makeText(this, "Enter Your Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    // call the logIn function with the user entered mobile number
    private fun sendVerificationCode(number: String) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callback) // OnVerificationStateChangeCallBacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}