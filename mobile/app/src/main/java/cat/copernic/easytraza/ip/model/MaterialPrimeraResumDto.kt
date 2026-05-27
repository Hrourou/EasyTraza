package cat.copernic.easytraza.ip.model

import com.google.gson.annotations.SerializedName

data class MaterialPrimeraResumDto(
    @SerializedName("id") val id: Long,
    @SerializedName("nom") val nom: String,
    @SerializedName("descripcio") val descripcio: String? = null
)
