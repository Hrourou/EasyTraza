package cat.copernic.easytraza.lot.data.repository

import cat.copernic.easytraza.lot.data.api.LotService
import cat.copernic.easytraza.lot.model.LotDto

class LotRepositoryImpl(private val apiService: LotService) : LotRepository {

    override suspend fun getLotsEnEstoc(): Result<List<LotDto>> {
        return try {
            val response = apiService.getLotsPerEstat("EN_ESTOC")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al carregar lots en estoc"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLotsOberts(): Result<List<LotDto>> {
        return try {
            val response = apiService.getLotsPerEstat("OBERT")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al carregar lots oberts"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun iniciarLot(proveidorCif: String, id: String, confirmarTancament: Boolean, nifUsuari: String?): Result<String> {
        return try {
            val response = apiService.iniciarLot(proveidorCif, id, confirmarTancament, nifUsuari)
            when (response.code()) {
                200 -> Result.success("Lot iniciat correctament")
                409 -> Result.failure(Exception("CONFLICTE_LOT_OBERT"))
                else -> Result.failure(Exception("Error al iniciar el lot"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun finalitzarLot(proveidorCif: String, id: String, nifUsuari: String?): Result<String> {
        return try {
            val response = apiService.finalitzarLot(proveidorCif, id, nifUsuari)
            if (response.isSuccessful) {
                Result.success("Lot finalitzat correctament")
            } else {
                Result.failure(Exception("Error al finalitzar el lot"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
