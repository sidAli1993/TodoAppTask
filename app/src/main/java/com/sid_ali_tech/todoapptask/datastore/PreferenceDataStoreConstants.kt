package com.sid_ali_tech.todoapptask.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceDataStoreConstants {
    val TODOS = stringPreferencesKey("TODOS")
    val TODOS_POS = intPreferencesKey("TODOS_POS")
    val IS_DARK = booleanPreferencesKey("IS_DARK")
    val TODOS_LIST = stringPreferencesKey("TODOS_LIST")
}