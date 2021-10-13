package honey.download

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val downloadFile: Button = findViewById(R.id.downloadButton)
        downloadFile.setOnClickListener {
            downloadFileUsingRetrofit()
        }
    }

    private fun downloadFileUsingRetrofit() {
        val downloadService = create()
        val call: Call<ResponseBody> = downloadService.downloadFile()
    }

    private fun create(): FileDownloader {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://api-sandbox.simple.org/api-docs#tag/analytics/")
                .build()

        return retrofit.create(FileDownloader::class.java)
    }
}