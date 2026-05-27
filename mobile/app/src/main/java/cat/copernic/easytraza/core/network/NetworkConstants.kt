package cat.copernic.easytraza.core.network

object NetworkConstants {
    // 10.0.2.2 es el localhost mágico del emulador de Android
    const val DEFAULT_IP = "10.0.2.2"
    const val PORT = "8080"

    fun buildBaseUrl(ip: String): String {
        return "http://$ip:$PORT/"
    }
}
