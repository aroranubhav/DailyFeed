package com.maxi.dailyfeed.framework.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.maxi.dailyfeed.domain.repository.AppDataStore
import com.maxi.dailyfeed.framework.datastore.DataStoreConstants.APP_PREFERENCES
import com.maxi.dailyfeed.framework.datastore.DataStoreConstants.E_TAG
import com.maxi.dailyfeed.framework.datastore.DataStoreConstants.IS_ALREADY_LAUNCHED
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

object DataStoreConstants {

    const val APP_PREFERENCES = "app_preferences"

    const val IS_ALREADY_LAUNCHED = "is_already_launched"
    const val E_TAG = "e_tag"
}

private val Context.appPreferences by preferencesDataStore(APP_PREFERENCES)

class DefaultAppDataStore(
    private val context: Context
) : AppDataStore {

    private val isAlreadyLaunched = booleanPreferencesKey(IS_ALREADY_LAUNCHED)
    private val eTag = stringPreferencesKey(E_TAG)

    override suspend fun getIsAlreadyLaunched(): Boolean {
        return context.appPreferences.data.map {
            it[isAlreadyLaunched]
        }.first() ?: false
    }

    override suspend fun updateIsAlreadyLaunched() {
        context.appPreferences.edit { prefs ->
            prefs[isAlreadyLaunched] = true
        }
    }

    override suspend fun getETag(): String? {
        return context.appPreferences.data.map {
            it[eTag]
        }.firstOrNull()
    }

    override suspend fun saveETag(value: String) {
        val arr = IntArray(10) {0}
        arr[1] = 10

        val stk = mutableListOf<Pair<Int, Int>>()
        val (x, y) = stk.removeLast()

        context.appPreferences.edit { prefs ->
            prefs[eTag] = value
        }
    }
}