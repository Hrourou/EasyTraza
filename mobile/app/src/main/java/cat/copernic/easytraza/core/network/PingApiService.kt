package cat.copernic.easytraza.core.network

import retrofit2.http.GET

data class PingResponse(val status: String, val app: String)

interface PingApiService {
    @GET("api/ping")
    suspend fun ping(): PingResponse
}
