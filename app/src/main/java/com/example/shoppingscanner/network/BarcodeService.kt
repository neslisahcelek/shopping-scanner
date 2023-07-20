package com.example.shoppingscanner.network

import com.example.shoppingscanner.model.ProductResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BarcodeService {
    companion object {
        //private const val API_KEY = "82daf7245dmsh8a18bad16c35487p1de469jsnc17fa7f0456e"
    }

    @GET("BarkodServis.asmx")
     suspend fun getProductDetails(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("barkod") barcode: String = ""
    ): ProductResponse

}
private const val BASE_URL = "https://api.barcodelookup.com/v3/"
private const val API_KEY = "3nywf7rropykmux7dklbpewef4m9vk"
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

object barcodeAPI {
    val retrofitService : BarcodeAPIService by lazy {
        retrofit.create(BarcodeAPIService::class.java) }
}

interface BarcodeAPIService{
    @GET("products")
    suspend fun getProductDetails(
        @Query("barcode") barcode: String = "",
        @Query("key") format: String = API_KEY
    ): ProductResponse

}