package com.diego.hifive.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class DataStoreManager(context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="NAME")
    private val dataStore = context.dataStore

    companion object {
        var nameKey = stringPreferencesKey("NAME")
        var themeKey = stringPreferencesKey("THEME")
    }

    suspend fun setName(name: String) {
        dataStore.edit { pref ->
            pref[nameKey] = name
        }
    }

    suspend fun setTheme(theme: Boolean) {
        dataStore.edit { pref ->
            pref[themeKey] = theme.toString()
        }
    }

    fun getName() : Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val name = pref[nameKey] ?: ""
                name
            }
    }

    fun getTheme() : Flow<String> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                val theme = pref[themeKey] ?: ""
                theme
            }
    }
}