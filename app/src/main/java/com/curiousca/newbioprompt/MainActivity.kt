package com.curiousca.newbioprompt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private val TAG: String = "MAinActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "super.onCreate")

        biometricPrompt = bioMet23()
    }

    private fun bioMet23(): androidx.biometric.BiometricPrompt{
        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : androidx.biometric.BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "$errorCode :: $errString")

                if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    //finish()
                    Toast.makeText(this@MainActivity,
                        "Canceled by User pressing negative button", Toast.LENGTH_LONG).show()
                }
                else if (errorCode == androidx.biometric.BiometricPrompt.ERROR_CANCELED) {
                    //finish()
                    Toast.makeText(this@MainActivity, "Canceled", Toast.LENGTH_LONG).show()
                }
                else if (errorCode == androidx.biometric.BiometricPrompt.ERROR_HW_NOT_PRESENT) {
                    //finish()
                    Toast.makeText(this@MainActivity,
                        "No BioHardware on this device", Toast.LENGTH_LONG).show()
                }
                else if (errorCode == androidx.biometric.BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL) {
                    //finish()
                    Toast.makeText(this@MainActivity,
                        "No Biometric set up on this device", Toast.LENGTH_LONG).show()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication Failed")
                Toast.makeText(this@MainActivity, "Authentication Failed", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "***SUCCESS***")
                doLogin()
            }
        }
        return androidx.biometric.BiometricPrompt(this@MainActivity, executor, callback)

    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo{
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("BiometricPrompt")
            .setSubtitle("Login Using BiometricPrompt")
            .setNegativeButtonText("Cancel/Use Password")
            .build()
    }

    fun openBio(view: View) {
        Log.d(TAG, "<+++ BiometricPrompt Opened +++>")
        val promptInfo = createPromptInfo()
        if (BiometricManager
                .from(applicationContext)
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){
            biometricPrompt.authenticate(promptInfo)
        }else{
            loginWithPassword()
        }
    }

    private fun loginWithPassword(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun doLogin(){
        //TODO log into the application
    }
}
