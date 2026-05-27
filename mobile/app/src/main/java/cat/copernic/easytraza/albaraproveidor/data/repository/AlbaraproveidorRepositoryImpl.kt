package cat.copernic.easytraza.albaraproveidor.data.repository

import cat.copernic.easytraza.albaraproveidor.data.api.AlbaraproveidorService
import cat.copernic.easytraza.albaraproveidor.model.OcrRespostaDto
import cat.copernic.easytraza.ip.model.MaterialPrimeraResumDto
import cat.copernic.easytraza.ip.model.ProviderResumDto
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AlbaraproveidorRepositoryImpl(private val api: AlbaraproveidorService) : AlbaraproveidorRepository {

    override suspend fun getProveidors(): Result<List<ProviderResumDto>> {
        return try {
            val response = api.obtenirProveidors()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Error carregant proveïdors"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun getMateries(): Result<List<MaterialPrimeraResumDto>> {
        return try {
            val response = api.obtenirMateriesPrimeres()
            if (response.isSuccessful) Result.success(response.body() ?: emptyList())
            else Result.failure(Exception("Error carregant matèries"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun processarOcr(imageFile: File): Result<OcrRespostaDto> {
        return try {
            val mediaType = "image/jpeg".toMediaTypeOrNull()
            val requestFile = imageFile.asRequestBody(mediaType)
            val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

            val response = api.processarAlbaraOcr(body)
            if (response.isSuccessful && response.body() != null) Result.success(response.body()!!)
            else Result.failure(Exception("Error de l'OCR: ${response.errorBody()?.string()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun guardarAlbara(dto: OcrRespostaDto): Result<String> {
        return try {
            val response = api.guardarAlbara(dto)
            if (response.isSuccessful) Result.success("Guardat correctament")
            else Result.failure(Exception("Error al guardar: ${response.errorBody()?.string()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
