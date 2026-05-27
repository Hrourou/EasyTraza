package cat.copernic.easytraza.ip.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val serverIpFlow: Flow<String>
    suspend fun saveServerIp(ip: String)
    suspend fun guardarSessio(email: String, nom: String, rol: String)
    val usuariNomFlow: Flow<String?>
    val usuariEmailFlow: Flow<String?>
}
