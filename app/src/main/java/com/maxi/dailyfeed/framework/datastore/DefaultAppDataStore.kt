package com.maxi.dailyfeed.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.maxi.dailyfeed.domain.repository.AppDataStore
import com.maxi.dailyfeed.framework.datastore.DataStoreConstants.APP_PREFERENCES
import com.maxi.dailyfeed.framework.datastore.DataStoreConstants.IS_ALREADY_LAUNCHED
import kotlinx.coroutines.flow.first

object DataStoreConstants {

    const val APP_PREFERENCES = "app_preferences"

    const val IS_ALREADY_LAUNCHED = "is_already_launched"
}

private val Context.appPreferences by preferencesDataStore(APP_PREFERENCES)

class DefaultAppDataStore(
    private val context: Context
) : AppDataStore {

    private val isAlreadyLaunched = booleanPreferencesKey(IS_ALREADY_LAUNCHED)

    override suspend fun getIsAlreadyLaunched(): Boolean {
        val prefs = context.appPreferences.data.first()
        return prefs[isAlreadyLaunched] ?: false
    }

    override suspend fun updateIsAlreadyLaunched() {
        context.appPreferences.edit { prefs ->
            prefs[isAlreadyLaunched] = true
        }
    }
}