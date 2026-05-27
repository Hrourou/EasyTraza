package cat.copernic.easytraza.auth.data.api

import cat.copernic.easytraza.auth.model.UsuariDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST

interface LoginService {
    @GET("api/usuaris")
    suspend fun getUsuaris(): Response<List<UsuariDto>>

    @Multipart
    @POST("api/usuaris/{email}/foto")
    suspend fun uploadFotoPerfil(
        @retrofit2.http.Path("email") email: String,
        @retrofit2.http.Part file: okhttp3.MultipartBody.Part
    ): Response<Map<String, String>>
}
