package cat.copernic.easytraza.albaraproveidor.data.repository

import cat.copernic.easytraza.albaraproveidor.model.OcrRespostaDto
import cat.copernic.easytraza.ip.model.MaterialPrimeraResumDto
import cat.copernic.easytraza.ip.model.ProviderResumDto
import java.io.File

interface AlbaraproveidorRepository {
    suspend fun getProveidors(): Result<List<ProviderResumDto>>
    suspend fun getMateries(): Result<List<MaterialPrimeraResumDto>>
    suspend fun processarOcr(imageFile: File): Result<OcrRespostaDto>
    suspend fun guardarAlbara(dto: OcrRespostaDto): Result<String>
}
