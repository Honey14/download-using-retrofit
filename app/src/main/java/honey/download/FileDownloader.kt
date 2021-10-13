package honey.download

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface FileDownloader {

    @GET("emoji-emoticon-character-background-new-year-background_1419-2091.jpg")
    fun downloadFile(): Call<ResponseBody>
}
