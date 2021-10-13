package honey.download

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import android.util.Log
import android.widget.Toast
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.InputStream
import java.io.File

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

        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { writeResponseBodyToDisk(it) }
                    Toast
                        .makeText(this@MainActivity, "File has been downloaded", Toast.LENGTH_LONG)
                        .show()
                } else
                    Log.e("TAG", "was not successful")
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("TAG", "error")
            }
        })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            val spreadsheet =
                File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + File.separator + "spreadsheet.png")
            Log.e("TAG", spreadsheet.toString())

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(spreadsheet)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d("TAG", "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

    private fun create(): FileDownloader {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://image.freepik.com/free-vector/")
                .build()

        return retrofit.create(FileDownloader::class.java)
    }
}