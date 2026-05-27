package cat.copernic.easytraza.albaraproveidor.model

import com.google.gson.annotations.SerializedName

data class OcrRespostaDto(
    @SerializedName("cifProveidor") var cifProveidor: String = "",
    @SerializedName("nomProveidor") var nomProveidor: String? = null,
    @SerializedName("dataRecepcio") var dataRecepcio: String = "",
    @SerializedName("fotoAlbaraUrl") var fotoAlbaraUrl: String? = null,
    @SerializedName("liniesLote") var liniesLote: List<OcrLiniaRespostaDto> = emptyList()
)
