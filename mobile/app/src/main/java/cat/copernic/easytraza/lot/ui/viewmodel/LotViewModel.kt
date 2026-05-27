package cat.copernic.easytraza.lot.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytraza.ip.data.repository.SettingsRepository
import cat.copernic.easytraza.lot.data.repository.LotRepository
import cat.copernic.easytraza.lot.model.LotDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LotViewModel(
    private val repository: LotRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _lotsEnEstoc = MutableStateFlow<List<LotDto>>(emptyList())
    val lotsEnEstoc: StateFlow<List<LotDto>> = _lotsEnEstoc

    private val _lotsOberts = MutableStateFlow<List<LotDto>>(emptyList())
    val lotsOberts: StateFlow<List<LotDto>> = _lotsOberts

    var lotSeleccionat by mutableStateOf<LotDto?>(null)
        private set

    var lotPendentDIniciar by mutableStateOf<LotDto?>(null)
        private set

    var missatgeError by mutableStateOf<String?>(null)
        private set

    fun carregarDades() {
        viewModelScope.launch {
            repository.getLotsEnEstoc().onSuccess {
                _lotsEnEstoc.value = it
            }.onFailure {
                missatgeError = "Error al carregar lots en estoc"
            }

            repository.getLotsOberts().onSuccess {
                _lotsOberts.value = it
            }.onFailure {
                missatgeError = "Error al carregar lots oberts"
            }
        }
    }

    fun recarregarDades() {
        carregarDades()
    }

    fun seleccionarLot(lot: LotDto) {
        lotSeleccionat = lot
    }

    fun netejarAvisos() {
        missatgeError = null
        lotPendentDIniciar = null
    }

    fun iniciarLot(lot: LotDto, confirmar: Boolean, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val email = settingsRepository.usuariEmailFlow.first()
            val proveidorCif = lot.proveidorCif ?: "" // Handle null case if any
            repository.iniciarLot(proveidorCif, lot.id, confirmar, email).onSuccess {
                carregarDades()
                onSuccess()
            }.onFailure { e ->
                if (e.message == "CONFLICTE_LOT_OBERT") {
                    lotPendentDIniciar = lot
                } else {
                    missatgeError = e.message ?: "Error al iniciar el lot"
                }
            }
        }
    }

    fun finalitzarLot(proveidorCif: String, id: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val email = settingsRepository.usuariEmailFlow.first()
            repository.finalitzarLot(proveidorCif, id, email).onSuccess {
                carregarDades()
                onSuccess()
            }.onFailure {
                missatgeError = it.message ?: "Error al finalitzar el lot"
            }
        }
    }
}
