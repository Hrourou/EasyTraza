package cat.copernic.easytraza.albaraproveidor.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.copernic.easytraza.albaraproveidor.data.repository.AlbaraproveidorRepository
import cat.copernic.easytraza.albaraproveidor.model.OcrLiniaRespostaDto
import cat.copernic.easytraza.ip.model.MaterialPrimeraResumDto
import cat.copernic.easytraza.ip.model.ProviderResumDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException

class AlbaraproveidorViewModel(private val repository: AlbaraproveidorRepository) : ViewModel() {

    var llistaProveidors by mutableStateOf<List<ProviderResumDto>>(emptyList())
        private set
    var llistaMateries by mutableStateOf<List<MaterialPrimeraResumDto>>(emptyList())
        private set

    var formulari by mutableStateOf(AlbaraFormulariEstat())
        private set

    var uiState by mutableStateOf<AlbaraproveidorUiState>(AlbaraproveidorUiState.Idle)
        private set

    var isProveidorExistent by mutableStateOf(false)
        private set

    var isNouProveidor by mutableStateOf(false)
        private set

    private var ocrJob: Job? = null

    init {
        carregarLlistesDesplegables()
    }

    fun carregarLlistesDesplegables() {
        viewModelScope.launch {
            repository.getProveidors().onSuccess { llistaProveidors = it }
            repository.getMateries().onSuccess { llistaMateries = it }
        }
    }

    fun processarImatgeOcr(context: Context, uri: Uri) {
        ocrJob = viewModelScope.launch {
            uiState = AlbaraproveidorUiState.Loading
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val tempFile = File(context.cacheDir, "ocr_temp.jpg")
                tempFile.outputStream().use { output -> inputStream?.copyTo(output) }

                val result = withTimeout(60_000) {
                    repository.processarOcr(tempFile)
                }

                result.onSuccess { dadesOcr ->
                    formulari = dadesOcr.toEstat()
                    comprovarEstatProveidor(dadesOcr.cifProveidor)
                    uiState = AlbaraproveidorUiState.Success("OCR completado con éxito")
                }.onFailure { error ->
                    uiState = AlbaraproveidorUiState.Error(mapNetworkError(error))
                }
            } catch (e: Exception) {
                uiState = AlbaraproveidorUiState.Error(mapNetworkError(e))
            }
        }
    }

    fun cancelOcr() {
        ocrJob?.cancel()
        ocrJob = null
        uiState = AlbaraproveidorUiState.Idle
    }

    private fun mapNetworkError(error: Throwable): String {
        return when (error) {
            is ConnectException -> "No s'ha pogut connectar amb el servidor."
            is SocketTimeoutException -> "El servidor no ha respost a temps."
            is TimeoutCancellationException -> "L'operació ha superat el temps límit."
            else -> {
                val msg = error.message ?: ""
                if (msg.contains("Data truncation", ignoreCase = true) && msg.contains("cif", ignoreCase = true)) {
                    "El CIF introduït no és vàlid o supera el límit permès."
                } else if (msg.startsWith("Error al guardar: {\"error\"")) {
                    "Error al guardar les dades. Verifica que el formulari sigui correcte."
                } else {
                    msg.ifEmpty { "S'ha produït un error inesperat." }
                }
            }
        }
    }

    fun updateCif(nouCif: String) {
        val cifNet = nouCif.uppercase().trim()
        val proveidorTrobat = llistaProveidors.find { it.cif.equals(cifNet, ignoreCase = true) }

        if (proveidorTrobat != null) {
            isProveidorExistent = true
            isNouProveidor = false
            formulari = formulari.copy(cifProveidor = proveidorTrobat.cif, nomProveidor = proveidorTrobat.nom)
        } else {
            isProveidorExistent = false
            isNouProveidor = cifNet.isNotEmpty()
            formulari = formulari.copy(cifProveidor = cifNet)
        }
    }

    private fun comprovarEstatProveidor(cif: String) {
        val cifNet = cif.uppercase().trim()
        val proveidorTrobat = llistaProveidors.find { it.cif.equals(cifNet, ignoreCase = true) }
        if (proveidorTrobat != null) {
            isProveidorExistent = true
            isNouProveidor = false
            formulari = formulari.copy(nomProveidor = proveidorTrobat.nom)
        } else {
            isProveidorExistent = false
            isNouProveidor = cifNet.isNotEmpty()
        }
    }

    fun updateNomProveidor(nom: String) { formulari = formulari.copy(nomProveidor = nom) }
    fun updateData(data: String) { formulari = formulari.copy(dataRecepcio = data) }

    fun updateMateria(index: Int, materiaNom: String) {
        val linies = formulari.liniesLote.toMutableList()
        val materiaTrobada = llistaMateries.find { it.nom.equals(materiaNom, ignoreCase = true) }
        linies[index] = linies[index].copy(
            nomMateriaPrimeraDetectada = materiaNom,
            materiaPrimeraId = materiaTrobada?.id
        )
        formulari = formulari.copy(liniesLote = linies)
    }

    fun updateLot(index: Int, lotId: String) {
        val linies = formulari.liniesLote.toMutableList()
        linies[index] = linies[index].copy(idLot = lotId)
        formulari = formulari.copy(liniesLote = linies)
    }

    fun updateUnitats(index: Int, unitatsStr: String) {
        val linies = formulari.liniesLote.toMutableList()
        linies[index] = linies[index].copy(unitats = unitatsStr.toIntOrNull())
        formulari = formulari.copy(liniesLote = linies)
    }

    fun addLiniaLote() {
        val linies = formulari.liniesLote.toMutableList()
        linies.add(OcrLiniaRespostaDto())
        formulari = formulari.copy(liniesLote = linies)
    }

    fun removeLiniaLote(index: Int) {
        val linies = formulari.liniesLote.toMutableList()
        if (linies.size > 1) {
            linies.removeAt(index)
            formulari = formulari.copy(liniesLote = linies)
        }
    }

    fun guardarAlbara() {
        viewModelScope.launch {
            uiState = AlbaraproveidorUiState.Loading
            repository.guardarAlbara(formulari.toDto()).onSuccess {
                uiState = AlbaraproveidorUiState.Success("Albarà guardat correctament")
                formulari = AlbaraFormulariEstat()
                isNouProveidor = false
                isProveidorExistent = false
                carregarLlistesDesplegables()
            }.onFailure {
                uiState = AlbaraproveidorUiState.Error(mapNetworkError(it))
            }
        }
    }

    fun netejarAvisos() {
        uiState = AlbaraproveidorUiState.Idle
    }
}
