package com.laurentdarl.associatedev.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.laurentdarl.associatedev.domain.models.UserDetails
import kotlinx.coroutines.flow.map

    val Context.datastore: DataStore<Preferences> by preferencesDataStore("userData")

class UserPreferences(val context: Context) {
    companion object {
        val NAME = stringPreferencesKey("NAME")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
    }

    suspend fun saveData(userDetails: UserDetails) {
        context.datastore.edit {
            it[NAME] = userDetails.name.toString()
            it[EMAIL] = userDetails.email.toString()
            it[PASSWORD] = userDetails.password.toString()
        }
    }

    suspend fun getFromDataStore() = context.datastore.data.map {
        UserDetails(
            name = it[NAME] ?: "",
            email = it[EMAIL] ?: "",
            password = it[PASSWORD] ?: ""
        )
    }
}