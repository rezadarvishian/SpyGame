package da.reza.spy.di


import com.google.gson.GsonBuilder
import da.reza.spy.data.remote.ImageApi
import da.reza.spy.utiles.ConstVal.CONNECT_TIMEOUT
import da.reza.spy.utiles.ConstVal.PIXABAY_BASE_URL
import da.reza.spy.utiles.ConstVal.READ_TIMEOUT
import da.reza.spy.utiles.ConstVal.VAZHEYAB__BASE_URL
import da.reza.spy.utiles.ConstVal.WRITE_TIMEOUT
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val apiModule = module {

    single {
        GsonBuilder().create()
    }


    single {
        OkHttpClient.Builder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }.build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(VAZHEYAB__BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }


    single {
        get<Retrofit>()
            .create(ImageApi::class.java)
    }


}