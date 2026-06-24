package com.example.autocare.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.autocare.data.session.SessionManager

val Context.dataStore : DataStore<Preferences> by preferencesDataStore("prefs")
class AppContainer(val context : Context) {

    val dataStore : DataStore<Preferences> = context.dataStore

    val sessionManager : SessionManager by lazy{
        SessionManager(dataStore)
    }
}