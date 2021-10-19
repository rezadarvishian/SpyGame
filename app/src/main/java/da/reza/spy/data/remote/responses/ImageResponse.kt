package da.reza.spy.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("hits")
    val imageList : List<ImageResult> ,
    val total :Int,
    @SerializedName("totalHits")
    val totalImage : Int
)
