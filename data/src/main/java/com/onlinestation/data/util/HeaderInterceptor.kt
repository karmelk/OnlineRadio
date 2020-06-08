package com.onlinestation.data.util


import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinComponent


class HeaderInterceptor() : Interceptor,
        KoinComponent {
    lateinit var request: Request
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        request = if (originalRequest.url.toString().contains("/identity/login-mobile")) {
            val requestBuilder = originalRequest.newBuilder()
                    .method(originalRequest.method, originalRequest.body)
            requestBuilder.build()

        } else {
            val requestBuilder = originalRequest.newBuilder()
                    .method(originalRequest.method, originalRequest.body)
            requestBuilder.build()
        }


        return chain.proceed(request)

    }


    private fun Request.Builder.addHeaders(token: String) =
            this.apply { header("Authorization", "Bearer $token") }

}