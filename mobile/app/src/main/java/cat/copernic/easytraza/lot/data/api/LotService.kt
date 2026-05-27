package cat.copernic.easytraza.lot.data.api

import cat.copernic.easytraza.lot.model.LotDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LotService {
    @GET("api/lots/estat/{estat}")
    suspend fun getLotsPerEstat(@Path("estat") estat: String): Response<List<LotDto>>

    @POST("api/lots/{proveidorCif}/{id}/iniciar")
    suspend fun iniciarLot(
        @Path("proveidorCif") proveidorCif: String,
        @Path("id") id: String,
        @Query("confirmar") confirmar: Boolean = false,
        @Query("nifUsuari") nifUsuari: String? = null
    ): Response<Map<String, String>>

    @POST("api/lots/{proveidorCif}/{id}/finalitzar")
    suspend fun finalitzarLot(
        @Path("proveidorCif") proveidorCif: String,
        @Path("id") id: String,
        @Query("nifUsuari") nifUsuari: String? = null
    ): Response<Map<String, String>>
}
