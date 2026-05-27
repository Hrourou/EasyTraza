package cat.copernic.easytraza.ip.model

import com.google.gson.annotations.SerializedName

data class ProviderResumDto(
    @SerializedName("cif") val cif: String,
    @SerializedName("nom") val nom: String?
)
