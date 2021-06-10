package com.vaca.netdisk

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.io.File
import java.lang.Exception
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

        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), RequestSinglePhoto
        )


        dataScope.launch {
            try {
                NetCmd.ga(File(PathUtil.getPathX("fuck.txt")))
            }catch (e:Exception){

            }

        }
    }


}