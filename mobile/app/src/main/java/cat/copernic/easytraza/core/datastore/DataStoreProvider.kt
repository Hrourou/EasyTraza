package cat.copernic.easytraza.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Extensión para tener un Singleton del DataStore en toda la app
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
