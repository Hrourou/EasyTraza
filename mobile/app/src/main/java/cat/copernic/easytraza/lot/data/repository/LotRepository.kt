package cat.copernic.easytraza.lot.data.repository

import cat.copernic.easytraza.lot.model.LotDto

interface LotRepository {
    suspend fun getLotsEnEstoc(): Result<List<LotDto>>
    suspend fun getLotsOberts(): Result<List<LotDto>>
    suspend fun iniciarLot(proveidorCif: String, id: String, confirmarTancament: Boolean, nifUsuari: String?): Result<String>
    suspend fun finalitzarLot(proveidorCif: String, id: String, nifUsuari: String?): Result<String>
}
