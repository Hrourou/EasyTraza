package cat.copernic.easytraza.albaraproveidor.model

import cat.copernic.easytraza.albaraproveidor.data.repository.AlbaraproveidorRepository

class DesarAlbaraUseCase(private val repository: AlbaraproveidorRepository) {
    suspend operator fun invoke(dto: OcrRespostaDto): Result<String> {
        return repository.guardarAlbara(dto)
    }
}
