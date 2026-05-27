package cat.copernic.easytraza.ip.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytraza.core.network.PingApiService
import cat.copernic.easytraza.core.network.RetrofitClient
import cat.copernic.easytraza.ip.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class IpViewModel(
    private val repository: SettingsRepository
) : ViewModel() {

    private val _ipText = MutableStateFlow("")
    val ipText: StateFlow<String> = _ipText

    private val _connectionStatus = MutableStateFlow("")
    val connectionStatus: StateFlow<String> = _connectionStatus

    private val pingApi by lazy { RetrofitClient.createService(PingApiService::class.java, repository) }

    init {
        viewModelScope.launch {
            _ipText.value = repository.serverIpFlow.first()
        }
    }

    fun onIpChange(newIp: String) {
        _ipText.value = newIp
    }

    fun saveIp() {
        viewModelScope.launch {
            repository.saveServerIp(_ipText.value)
            _connectionStatus.value = "Configuració desada localment."
        }
    }

    fun testConnection() {
        viewModelScope.launch {
            _connectionStatus.value = "Provant connexió..."
            try {
                repository.saveServerIp(_ipText.value)
                val response = pingApi.ping()
                if (response.status == "ok") {
                    _connectionStatus.value = "Connexió correcta! App: ${response.app}"
                } else {
                    _connectionStatus.value = "La resposta no és correcta."
                }
            } catch (e: Exception) {
                _connectionStatus.value = "Error de connexió: ${e.localizedMessage}"
            }
        }
    }
}
