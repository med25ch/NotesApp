package com.resse.notesapp.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//URL
private const val BASE_URL_BORED = "https://www.boredapi.com/api/"

// Moshi Builder
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


//Retrofit builder
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_BORED)
    .build()


interface BoredApiService {
    @GET("activity")
    suspend fun getActivity(@Query("type") type:String) : BoredActivity
}


object BoredApi {
    val retrofitService : BoredApiService by lazy {
        retrofit .create(BoredApiService::class.java)
    }
}
