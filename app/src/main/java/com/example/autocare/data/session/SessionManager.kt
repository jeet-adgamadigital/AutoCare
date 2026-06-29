package com.example.autocare.data.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionManager(private val dataStore : DataStore<Preferences>) {

    companion object{
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        val IS_NOTIFICATION_ENABLED = booleanPreferencesKey("is_notification_enabled")
    }

    val isLoggedIn : Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }

    suspend fun setLoginStatus(status : Boolean){
        dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = status
        }
    }

    val isNotificationEnabled : Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[IS_NOTIFICATION_ENABLED] ?: false
        }

    suspend fun setNotificationStatus(status: Boolean){
        dataStore.edit { preferences ->
            preferences[IS_NOTIFICATION_ENABLED] = status
        }
    }
}