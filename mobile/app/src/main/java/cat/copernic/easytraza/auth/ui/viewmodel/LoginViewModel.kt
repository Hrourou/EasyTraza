package cat.copernic.easytraza.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytraza.auth.data.api.LoginService
import cat.copernic.easytraza.auth.model.UsuariDto
import cat.copernic.easytraza.core.network.RetrofitClient
import cat.copernic.easytraza.ip.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class LoginViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _usuaris = MutableStateFlow<List<UsuariDto>>(emptyList())
    val usuaris: StateFlow<List<UsuariDto>> = _usuaris

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val apiService = RetrofitClient.createService(LoginService::class.java, settingsRepository)

    init {
        carregarUsuaris()
    }

    fun carregarUsuaris() {
        viewModelScope.launch {
            _isError.value = false
            try {
                val response = apiService.getUsuaris()
                if (response.isSuccessful) {
                    _usuaris.value = response.body() ?: emptyList()
                    _isError.value = _usuaris.value.isEmpty()
                } else {
                    _isError.value = true
                }
            } catch (e: Exception) {
                _isError.value = true
            }
        }
    }

    fun iniciarSessio(usuari: UsuariDto, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            settingsRepository.guardarSessio(usuari.email, usuari.nom, usuari.rol)
            onLoginSuccess()
        }
    }

    val serverIpFlow: kotlinx.coroutines.flow.Flow<String> = settingsRepository.serverIpFlow

    fun actualizarFotoPerfil(context: android.content.Context, uri: android.net.Uri, email: String) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = java.io.File(context.cacheDir, "profile_temp.jpg")
                tempFile.outputStream().use { output -> inputStream?.copyTo(output) }

                val mediaType = "image/jpeg".toMediaTypeOrNull()
                val requestFile = tempFile.asRequestBody(mediaType)
                val body = okhttp3.MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

                val response = apiService.uploadFotoPerfil(email, body)
                if (response.isSuccessful) {
                    carregarUsuaris() // Recargar para obtener la nueva URL
                }
            } catch (e: Exception) {
                // Manejar error de subida si es necesario
            }
        }
    }
}
