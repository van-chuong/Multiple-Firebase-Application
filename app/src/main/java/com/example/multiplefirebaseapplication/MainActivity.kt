package com.example.multiplefirebaseapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseOptions
import com.google.firebase.app
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private var firebaseConfig: FirebaseConfig? = null
    private var dbChild: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnGetChild = findViewById<Button>(R.id.btnGetChild)
        val btnCreateChild = findViewById<Button>(R.id.btnCreateChild)
        val btnGetDataChild = findViewById<Button>(R.id.btnGetDataChild)
        btnGetChild.setOnClickListener {
            getDocumentData()
        }
        btnCreateChild.setOnClickListener {
            createNewInstance()
        }
        btnGetDataChild.setOnClickListener {
            getChildDocumentData()
        }
    }

    private fun createNewInstance() {
        try {
            val options = firebaseConfig?.applicationId?.let {
                firebaseConfig?.apiKey?.let { it1 ->
                    FirebaseOptions.Builder()
                        .setProjectId(firebaseConfig?.projectId)
                        .setApplicationId(it)
                        .setApiKey(it1)
                        .setDatabaseUrl(firebaseConfig?.databaseUrl)
                        .setStorageBucket(firebaseConfig?.storageBucket)
                        .build()
                }
            }
            // Initialize secondary FirebaseApp.
            if (options != null) {
                Firebase.initialize(context = this, options, "secondary")
            }
            // Retrieve secondary FirebaseApp.
            val secondary = Firebase.app("secondary")
            // Get the database for the other app.
            dbChild = Firebase.firestore(secondary)
            Toast.makeText(this, "Kết nối firebase child thành công", Toast.LENGTH_SHORT).show()
        }catch (e:Exception){
            Toast.makeText(this, "Eror:${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getDocumentData() {
        db.collection("child_firebase")
            .limit(1)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Toast.makeText(
                            this,
                            document.id + " => " + document.getData(),
                            Toast.LENGTH_SHORT
                        ).show()
                        firebaseConfig = document.getData().toFirebaseConfig()
                        Log.d("OKKKK",firebaseConfig.toString())
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Lỗi khi lấy dữ liệu:${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun getChildDocumentData() {
        dbChild?.collection("test")
            ?.limit(1)
            ?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Toast.makeText(
                            this,
                            document.id + " => " + document.getData(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Lỗi khi lấy dữ liệu:${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}