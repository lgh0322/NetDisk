package com.vaca.netdisk.net

import android.util.Log
import com.google.gson.Gson
import com.vaca.netdisk.utils.PathUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*


object NetCmd {
    private val client = OkHttpClient();
    private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val JSON2: MediaType? = "multipart/form-data; charset=utf-8".toMediaTypeOrNull()

    private const val appId: String = "test"
    private const val nonce: String = "345"
    private const val secret: String = "VMRwHMDM--19ZaCNXNWVOxqI"
    var token: String = ""
    val netAddress: String = "http://192.168.6.103:3000"


    interface OnDownloadListener {
        /**
         * 开始下载
         */
        fun onDownloadStart()

        /**
         * 下载成功
         * @param filePath 文件下载的路径
         */
        fun onDownloadSuccess(filePath: String?)

        /**
         * @param progress 下载进度
         */
        fun onDownloading(progress: Int)

        /**
         * 下载失败
         */
        fun onDownloadFailed()
    }


    fun getFile(url: String, fileName: String, listener: OnDownloadListener?) {
        val absoluteFilePath: String = PathUtil.getPathX(fileName)
        val file = File(absoluteFilePath)
        val request: Request = Request.Builder().url(url).build()
        listener?.onDownloadStart()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                listener?.onDownloadFailed()
            }

            override fun onResponse(call: Call, response: Response) {
                if (200 == response.code) {
                    var fileOutputStream: FileOutputStream? = null
                    var inputStream: InputStream? = null
                    try {
                        val total = response.body!!.contentLength()
                        var sum: Long = 0
                        inputStream = response.body!!.byteStream()
                        fileOutputStream = FileOutputStream(file)
                        val buffer = ByteArray(1024 * 1024)
                        var len = 0
                        while (inputStream.read(buffer).also { len = it } != -1) {
                            fileOutputStream.write(buffer, 0, len)
                            sum += len.toLong()
                            val progress = (sum * 1.0f / total * 100).toInt()
                            // 下载中
                            listener?.onDownloading(progress)
                        }
                        fileOutputStream.flush()
                        listener?.onDownloadSuccess(absoluteFilePath)
                    } catch (e: IOException) {
                        listener?.onDownloadFailed()
                    } finally {
                        inputStream?.close()
                        fileOutputStream?.close()
                    }
                } else {
                    listener?.onDownloadFailed()
                }
            }

        }


        )
    }






    @Throws(IOException::class)
    fun uploadFile2(): String? {
        val url = "http://65.49.212.218:9000/echoPost"

        val bodyx="{\n" +
                "  \"input\":{\n" +
                "    \"text\":\"hello helicopter.\"\n" +
                "  },\n" +
                "  \"voice\":{\n" +
                "    \"languageCode\":\"en-gb\",\n" +
                "    \"name\":\"en-GB-Standard-A\",\n" +
                "    \"ssmlGender\":\"FEMALE\"\n" +
                "  },\n" +
                "  \"audioConfig\":{\n" +
                "    \"audioEncoding\":\"MP3\"\n" +
                "  }\n" +
                "}"

        val body2=bodyx.toRequestBody(JSON)


        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/json; charset=UTF-8")
            .url(url).post(body2)
            .build()
        client.newCall(request)
            .execute()
            .use { response ->
                response.body?.string()?.let { Log.e("sdf", it);return it }
            }
        return null
    }


    @Throws(IOException::class)
    fun uploadFile3(s:String): String? {
       // val url = "http://65.49.212.218:9000/echoPost2"
        val url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyD1nsDTghDkS21v5IU0RDTmKYq4mEB9FK8"


        var fuck=JSONObject()
        var fuck2=JSONArray()
        fuck2.put("come here")
        fuck.put("source","zh")
        fuck.put("target","en")
        fuck.put("format","text")
        fuck.put("q",s)
        val bo=fuck.toString()


        val body2=bo.toRequestBody(JSON)


        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/json; charset=UTF-8")
            .url(url).post(body2)
            .build()
        client.newCall(request)
            .execute()
            .use { response ->
                response.body?.string()?.let { Log.e("sdf", it);return it }
            }
        return null
    }



}