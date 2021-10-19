package da.reza.spy.data.remote

import da.reza.spy.BuildConfig
import da.reza.spy.data.remote.responses.ImageResponse
import da.reza.spy.data.remote.responses.VazheYabResponse
import da.reza.spy.utiles.ConstVal.PIXABAY_BASE_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ImageApi {


    /** init this based on image api doc
     * .... here we used pixabay image api -> https://pixabay.com/api/docs/
     * ... added your api key in gradle.properties as a field -> API_KEY = "your key" */

    @GET
    suspend fun searchForImage(
        @Url url:String = PIXABAY_BASE_URL,
        @Query("q") searchString :String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ):Response<ImageResponse>


    @GET("/v3/suggest")
    suspend fun suggestWord(
        @Query("token") token:String = BuildConfig.VAZHEYAB_TOKEN,
        @Query("q") word:String
    ):Response<VazheYabResponse>

    @GET("/v3/word")
    suspend fun wordMeaning(
        @Query("token") token:String = BuildConfig.VAZHEYAB_TOKEN,
        @Query("db") db:String = "dehkhoda",
        @Query("num") num:Int = 1,
        @Query("title") word:String
    ):Response<VazheYabResponse>

    @GET("/v3/search")
    suspend fun translateWord(
        @Query("token") token:String = BuildConfig.VAZHEYAB_TOKEN,
        @Query("filter") db:String = "en2fa",
        @Query("type") num:String = "text",
        @Query("q") word:String
    ):Response<VazheYabResponse>

}