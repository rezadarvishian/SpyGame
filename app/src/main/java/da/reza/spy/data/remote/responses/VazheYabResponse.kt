package da.reza.spy.data.remote.responses

import com.google.gson.annotations.SerializedName

data class VazheYabResponse (
    @SerializedName("response") val response : Response,
    @SerializedName("meta") val meta : Meta,
    @SerializedName(value = "data" , alternate = ["word"]) val data : Data
        )

data class Response (

    @SerializedName("status") val status : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("state") val state : String
)

data class Meta (

    @SerializedName("q") val q : String
)

data class Data (

    @SerializedName("suggestion") val suggestion : List<String>,
    @SerializedName("text") val text : String,
    @SerializedName("num_found") val NumberFound : Int,
    @SerializedName("results") val results : List<DictionaryRes>
)


data class DictionaryRes(
    @SerializedName("title_en") val word : String,
)