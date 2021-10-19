package da.reza.spy.utiles

import android.util.Log
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): BaseResult<T> {
        try {
            val response = call()
            Log.i("Response_Api", response.toString() + ", data=" + response.body())
            Log.i("Request_Api", response.raw().request().toString())


            if (response.code() != 200) {
                return BaseResult.error(message =  "image Service not Available")
            }

            if (response.isSuccessful) {
                val body = response.body()
                body?.apply {
                    return BaseResult.success(data = this)
                }
            } else return null!!


        } catch (e: Exception) {
            Log.i("Response_Api", "Exception while request : ${e.message.toString()}")
            return BaseResult.error(message =  "اینترنت قطعه :)")
        }
        return null!!
    }

}

data class BaseResult<out T>(val status: Status, val data: T?, val message : String?) {

    enum class Status { SUCCESS, ERROR }

    companion object {
        fun <T> success(data: T): BaseResult<T> = BaseResult(Status.SUCCESS, data , null)
        fun <T> error(message:String): BaseResult<T> = BaseResult(Status.ERROR, null , message)
    }
}