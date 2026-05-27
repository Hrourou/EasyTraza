package cat.copernic.easytraza.albaraproveidor.ui.viewmodel

sealed class AlbaraproveidorUiState {
    object Idle : AlbaraproveidorUiState()
    object Loading : AlbaraproveidorUiState()
    data class Success(val message: String) : AlbaraproveidorUiState()
    data class Error(val message: String) : AlbaraproveidorUiState()
}
