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
    private val TAGError: String = "AuthenticationError"
    private val TAGBio: String = "OpenBio"

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
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication Failed: print not recognized")
                Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_LONG).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "***SUCCESS***")
                doLogin()
            }
        }
        return androidx.biometric.BiometricPrompt(this, executor, callback)

    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo{
        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("BiometricPrompt")
            .setSubtitle("Login Using BiometricPrompt")
            .setNegativeButtonText("Cancel/Use Password")
            .build()
    }

    fun openBio(view: View) {
        val promptInfo = createPromptInfo()
        if (BiometricManager.BIOMETRIC_SUCCESS == BiometricManager
                .from(applicationContext)
                .canAuthenticate()){
            biometricPrompt.authenticate(promptInfo)
            Log.d(TAGBio, "<+++ BiometricPrompt Opened +++>")
        }else{
            loginWithPassword()
        }

        if (BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE == BiometricManager
                .from(applicationContext)
                .canAuthenticate()){
            Log.d(TAGBio, "This device does not have a biometric scanner.")
            Toast.makeText(applicationContext,
                "This device does not have a biometric scanner.", Toast.LENGTH_LONG).show()
        }
        if (BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED == BiometricManager
                .from(applicationContext)
                .canAuthenticate()){
            Log.d(TAGBio, "The user does not have any biometrics enrolled")
            Toast.makeText(applicationContext,
                "The user does not have any biometrics enrolled", Toast.LENGTH_LONG).show()

            //TODO take user to the biometric settings: likely show a dialog and give
            // the user a choice to set up biometrics or continue where they are.
        }
        if (BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE == BiometricManager
                .from(applicationContext)
                .canAuthenticate()){
            Log.d(TAGBio, "Biometric hardware is currently unavailable")
            Toast.makeText(applicationContext,
                "Biometric hardware is currently unavailable", Toast.LENGTH_LONG).show()
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
