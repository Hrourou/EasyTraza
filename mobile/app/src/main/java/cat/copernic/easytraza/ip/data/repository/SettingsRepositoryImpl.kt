package cat.copernic.easytraza.ip.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cat.copernic.easytraza.core.network.NetworkConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {

    private val IP_KEY = stringPreferencesKey("server_ip")

    override val serverIpFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[IP_KEY] ?: NetworkConstants.DEFAULT_IP
    }

    override suspend fun saveServerIp(ip: String) {
        dataStore.edit { preferences ->
            preferences[IP_KEY] = ip
        }
    }

    override val usuariNomFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[NOM_USUARI]
    }

    override val usuariEmailFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[EMAIL_USUARI]
    }

    override suspend fun guardarSessio(email: String, nom: String, rol: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_USUARI] = email
            preferences[NOM_USUARI] = nom
            preferences[ROL_USUARI] = rol
        }
    }

    companion object {
        val EMAIL_USUARI = stringPreferencesKey("email_usuari")
        val NOM_USUARI = stringPreferencesKey("nom_usuari")
        val ROL_USUARI = stringPreferencesKey("rol_usuari")
    }
}
