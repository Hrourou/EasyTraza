package cat.copernic.easytraza.albaraproveidor.data.api

import cat.copernic.easytraza.albaraproveidor.model.OcrRespostaDto
import cat.copernic.easytraza.ip.model.MaterialPrimeraResumDto
import cat.copernic.easytraza.ip.model.ProviderResumDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AlbaraproveidorService {
    @GET("api/recepcio/proveidors")
    suspend fun obtenirProveidors(): Response<List<ProviderResumDto>>

    @GET("api/recepcio/materies")
    suspend fun obtenirMateriesPrimeres(): Response<List<MaterialPrimeraResumDto>>

    @Multipart
    @POST("api/recepcio/ocr")
    suspend fun processarAlbaraOcr(
        @Part file: MultipartBody.Part
    ): Response<OcrRespostaDto>

    @POST("api/recepcio/save")
    suspend fun guardarAlbara(
        @Body dto: OcrRespostaDto
    ): Response<Map<String, String>>
}
