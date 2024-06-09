package relog.android.authentication.data.repository

import com.google.firebase.auth.AuthResult
import relog.android.authentication.others.Resource

interface AuthRepository {
    suspend fun register(email: String, name: String, password: String): Resource<AuthResult>
    suspend fun login(email: String, password: String): Resource<AuthResult>
}