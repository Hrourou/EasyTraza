package cat.copernic.easytraza.lot.model

import cat.copernic.easytraza.ip.model.MaterialPrimeraResumDto
import com.google.gson.annotations.SerializedName

data class LotDto(
    @SerializedName("id") val id: String,
    @SerializedName("proveidorCif") val proveidorCif: String?,
    @SerializedName("proveidorNom") val proveidorNom: String?,
    @SerializedName("estat") val estat: String,
    @SerializedName("quantitat") val quantitat: Double,
    @SerializedName("materiaPrimera") val materiaPrimera: MaterialPrimeraResumDto?
)
