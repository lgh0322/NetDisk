package com.vaca.netdisk

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap


object NetCmd {
    private val client = OkHttpClient();
    private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    private val JSON2: MediaType? = "multipart/form-data; charset=utf-8".toMediaTypeOrNull()

    private const val appId: String = "test"
    private const val nonce: String = "345"
    private const val secret: String = "VMRwHMDM--19ZaCNXNWVOxqI"
    var token: String = ""
    val netAddress: String = "http://192.168.5.100:3000"


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


    fun getAppUpdateFile(url: String, fileName: String, listener: OnDownloadListener?) {
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



    //-------------------------------------------------------------------------------删除血压数据接口
    @Throws(IOException::class)
    fun wa(): String? {
        val url = netAddress + "/login"
        val bodyTree = TreeMap<String, String>().apply {
            this["user"]="fuck"
            this["password"]="wer"
        }

        val body: RequestBody = Gson().toJson(bodyTree).toRequestBody(JSON)
        val request: Request = Request.Builder()
            .addHeader("Content-Type", "application/json; charset=UTF-8")
            .addHeader("appid", appId)
            .addHeader("token", token)
            .addHeader("nonce", nonce)
            .url(url).post(body)
            .build()
        client.newCall(request)
            .execute()
            .use { response ->
                response.body?.string()?.let { Log.e("sdf", it);return it }
            }
        return null
    }






}