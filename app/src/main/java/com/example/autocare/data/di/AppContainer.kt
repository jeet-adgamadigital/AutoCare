package com.example.autocare.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.autocare.data.remote.AuthRepository
import com.example.autocare.data.remote.AuthRepositoryImplementation
import com.example.autocare.data.room.AppDatabase
import com.example.autocare.data.session.SessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlin.getValue

val Context.dataStore : DataStore<Preferences> by preferencesDataStore("prefs")
class AppContainer(val context : Context) {

    val dataStore : DataStore<Preferences> = context.dataStore

    val sessionManager : SessionManager by lazy{
        SessionManager(dataStore)
    }

    val supabaseClient : SupabaseClient by lazy {
        createSupabaseClient(
            supabaseKey = "sb_publishable_mM8GUSGwcr3GTX3ZYH_DZQ_sfYJfCu4",
            supabaseUrl = "https://uwbqtsczvjmwugrtzwwu.supabase.co"
        ){
            install(Auth)
            install(Postgrest)
        }
    }

    val database : AppDatabase by lazy {
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    val authRepository : AuthRepository by lazy {
        AuthRepositoryImplementation(supabaseClient)
    }
}