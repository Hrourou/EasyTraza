package cat.copernic.easytraza.albaraproveidor.model

import com.google.gson.annotations.SerializedName

data class OcrLiniaRespostaDto(
    @SerializedName("nomMateriaPrimeraDetectada") var nomMateriaPrimeraDetectada: String? = null,
    @SerializedName("materiaPrimeraId") var materiaPrimeraId: Long? = null,
    @SerializedName("idLot") var idLot: String = "",
    @SerializedName("unitats") var unitats: Int? = null
)
