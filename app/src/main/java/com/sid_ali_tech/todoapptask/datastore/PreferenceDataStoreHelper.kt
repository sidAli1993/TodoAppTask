package com.sid_ali_tech.todoapptask.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(
    name = "PreferenceDataStore"
)
class PreferenceDataStoreHelper(context: Context): IPreferenceDataStoreAPI {

    private val dataSource = context.dataStore

    override suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
          Flow<T> = dataSource.data.catch { exception ->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map { preferences->
        val result = preferences[key]?: defaultValue
        result
    }


    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T) :
            T = dataSource.data.first()[key] ?: defaultValue

    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataSource.edit {   preferences ->
            preferences[key] = value
        }
    }

    override suspend fun saveUserToPreferencesStore(code: String, position: Int) {
        dataSource.edit { preferences ->
            preferences[PreferenceDataStoreConstants.TODOS] = code
            preferences[PreferenceDataStoreConstants.TODOS_POS] = position
        }
    }

    override suspend fun saveBooleanToDataStore(value: Boolean) {
        dataSource.edit { preferences ->
            preferences[PreferenceDataStoreConstants.IS_DARK] = value
        }
    }

    override fun getBooleanFromDataStore(): Flow<Boolean> {
        return dataSource.data.map { preferences ->
            preferences[PreferenceDataStoreConstants.IS_DARK] ?: false
        }
    }

    override suspend fun getUserFromPreferencesStore(): Flow<Pair<String, Int>> {
        return dataSource.data
            .map { preferences ->
                val code = preferences[PreferenceDataStoreConstants.TODOS] ?: "en"
                val position = preferences[PreferenceDataStoreConstants.TODOS_POS] ?: 0
                Pair(code, position)
            }
    }


    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataSource.edit { preferences ->
            preferences.remove(key)
        }
    }

    override suspend fun <T> clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }
}