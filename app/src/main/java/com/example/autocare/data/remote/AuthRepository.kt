package com.example.autocare.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

interface AuthRepository {
    suspend fun signUp(email : String, password : String, full_name : String) : Result<Unit>
    suspend fun signIn(email : String, password : String) : Result<Unit>
    suspend fun signOut() : Result<Unit>
}

class AuthRepositoryImplementation(private val supabaseClient: SupabaseClient) : AuthRepository{
    override suspend fun signUp(
        email: String,
        password: String,
        full_name: String
    ): Result<Unit> {
        return runCatching {
            supabaseClient.auth.signUpWith(Email){
                this.email = email
                this.password = password

                if(full_name.isNotBlank()){
                    this.data = buildJsonObject {
                        put("full_name", full_name)
                    }
                }
            }
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit> {
        return runCatching {
            supabaseClient.auth.signInWith(Email){
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return runCatching {
            supabaseClient.auth.signOut()
        }
    }

}