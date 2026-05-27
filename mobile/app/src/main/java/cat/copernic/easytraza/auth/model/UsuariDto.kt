package cat.copernic.easytraza.auth.model

import com.google.gson.annotations.SerializedName

data class UsuariDto(
    @SerializedName("email") val email: String,
    @SerializedName("nom") val nom: String,
    @SerializedName("cognom") val cognom: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("fotoPerfilUrl") val fotoPerfilUrl: String? = null
)
