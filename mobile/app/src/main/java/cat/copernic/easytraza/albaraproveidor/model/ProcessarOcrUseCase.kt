package cat.copernic.easytraza.albaraproveidor.model

import cat.copernic.easytraza.albaraproveidor.data.repository.AlbaraproveidorRepository
import java.io.File

class ProcessarOcrUseCase(private val repository: AlbaraproveidorRepository) {
    suspend operator fun invoke(imageFile: File): Result<OcrRespostaDto> {
        return repository.processarOcr(imageFile)
    }
}
