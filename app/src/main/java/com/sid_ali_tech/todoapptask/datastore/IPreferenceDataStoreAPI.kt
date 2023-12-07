package com.sid_ali_tech.todoapptask.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface IPreferenceDataStoreAPI {
    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<T>,defaultValue: T):T
    suspend fun <T> putPreference(key: Preferences.Key<T>,value:T)

    suspend fun  saveUserToPreferencesStore(code:String,position:Int)

    suspend fun saveBooleanToDataStore(value: Boolean)
    fun getBooleanFromDataStore(): Flow<Boolean>

    suspend fun getUserFromPreferencesStore(): Flow<Pair<String, Int>>

    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun <T> clearAllPreference()
}