package com.example.multiplefirebaseapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings


class MainActivity : AppCompatActivity() {

    private lateinit var txtTest: TextView

    // Initialize Firebase Remote Config
    val remoteConfig = Firebase.remoteConfig
    // Set default values (optional but recommended)
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600 // 1 hour, you can adjust as needed
    }

    // Method to fetch and activate config values
    private fun fetchRemoteConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Fetch the initial value of intro_enable and set the visibility
                    displayIntroMessage(remoteConfig.getBoolean("intro_enable"))
                } else {
                    Log.e("RemoteConfig", "Config params update failed")
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtTest = findViewById(R.id.txtTest)

        //Setup remote config
        remoteConfig.setConfigSettingsAsync(configSettings)
        fetchRemoteConfig()

        // Set up the config update listener
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("intro_enable")) {
                    // Fetch the updated value
                    val introEnable = remoteConfig.getBoolean("intro_enable")

                    // Call the method to display or hide the intro message
                    displayIntroMessage(introEnable)
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("RemoteConfig", "Config update error with code: ${error.code}", error)
            }
        })

    }

    private fun displayIntroMessage(isVisible: Boolean) {
        txtTest.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}