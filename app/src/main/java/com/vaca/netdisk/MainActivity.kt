package com.vaca.netdisk

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val RequestSinglePhoto = 2
    val dataScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PathUtil.initVar(this)


//        File(PathUtil.getPathX("fuck.txt")).writeBytes(byteArrayOf(0x61,0x62,0x63))
//
//
//        dataScope.launch {
//            try {
//                NetCmd.ga(File(PathUtil.getPathX("fuck.txt")))
//            }catch (e:Exception){
//
//            }
//        }
    }

    fun upload(view: View) {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), RequestSinglePhoto
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestSinglePhoto && resultCode == RESULT_OK && null != data) {
            try {
                val selectedImage = data.data
                val filePath: String =MainApplication. fileUtils.getPath(selectedImage)
                dataScope.launch {
                    NetCmd.ga(File(filePath))
                }

            }catch (e:java.lang.Exception){

            }
        }
    }


}