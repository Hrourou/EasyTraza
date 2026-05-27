package cat.copernic.easytraza.core.network

import cat.copernic.easytraza.ip.data.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class DynamicUrlInterceptor(private val repository: SettingsRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val currentIp = runBlocking { repository.serverIpFlow.first() }

        val newUrl = request.url.newBuilder().host(currentIp).build()
        val newRequest = request.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }
}
