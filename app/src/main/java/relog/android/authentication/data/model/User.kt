package relog.android.authentication.data.model

import com.google.firebase.firestore.IgnoreExtraProperties


@IgnoreExtraProperties
data class User(
    val uid: String = "",
    val name: String = ""
)
