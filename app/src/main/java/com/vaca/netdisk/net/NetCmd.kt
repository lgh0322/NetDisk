package com.vaca.netdisk.net

import android.util.Log
import com.google.gson.Gson
import com.vaca.netdisk.utils.PathUtil
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
    val netAddress: String = "http://192.168.5.101:3001"


    interface OnDownloadListener {
        fun onDownloadSuccess(filePath: String?)
        fun onDownloading(progress: Int)
        fun onDownloadFailed()
    }


    fun getFile(url: String, fileName: String, listener: OnDownloadListener?) {
        val absoluteFilePath: String = PathUtil.getPathX(fileName)
        val file = File(absoluteFilePath)
        val request: Request = Request.Builder()
            .addHeader("gaga","gaga2")
            .url(url).build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                listener?.onDownloadFailed()
            }
            override fun onResponse(call: Call, response: Response) {
                if (200 == response.code) {

                    Log.e("sdf",response.body!!.string())

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
    fun uploadFile(file: File,pro:UploadProgressListener): String? {
        val url = netAddress + "/upload"

        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart(
                "uploadFile", file.name,
                file.asRequestBody(JSON2)
            )


        val exMultipartBody = ExMultipartBody(builder.build(),pro)

        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/json; charset=UTF-8")
            .url(url).post(exMultipartBody)
            .build()
        client.newCall(request)
            .execute()
            .use { response ->
                response.body?.string()?.let { Log.e("sdf", it);return it }
            }
        return null
    }



}