package com.example.multiplefirebaseapplication
data class FirebaseConfig(
    val storageBucket: String,
    val projectId: String,
    val applicationId: String,
    val databaseUrl: String,
    val apiKey: String
)
fun Map<String, Any>.toFirebaseConfig(): FirebaseConfig {
    return FirebaseConfig(
        storageBucket = this["storageBucket"].toString(),
        projectId = this["projectId"].toString(),
        applicationId = this["applicationId"].toString(),
        databaseUrl = this["databaseUrl"].toString(),
        apiKey = this["apiKey"].toString()
    )
}