package com.vaca.netdisk.activity

import android.app.Activity
import android.content.Intent
import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.vaca.netdisk.MainApplication.Companion.fileUtils
import com.vaca.netdisk.databinding.ActivityMainBinding
import com.vaca.netdisk.net.NetCmd
import com.vaca.netdisk.net.UploadProgressListener
import com.vaca.netdisk.pop.UploadPop
import com.vaca.netdisk.utils.PathUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {












    private val REQUEST_CODE_SELECT_IMG = 91
    private val MAX_SELECT_COUNT = 20
    val uploadFuck=MutableLiveData<Int>()

    lateinit var binding: ActivityMainBinding
    var uploadPop:UploadPop?=null
    private val RequestSinglePhoto = 2
    val dataScope = CoroutineScope(Dispatchers.IO)



    var asyncPlayer = AsyncPlayer(null)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PathUtil.initVar(this)

        uploadFuck.observe(this,{
            uploadPop?.step2(it)
        })

        val decoder = Base64.getDecoder()
        dataScope.launch {

            try {
                val gg=NetCmd.uploadFile3()!!
//                val ggx=JSONObject(gg)
//                val audioContent=ggx.getString("audioContent")
//                val fuck=decoder.decode(audioContent)
//
//                val tempMp3 = File.createTempFile("kurchina", "mp3", cacheDir)
//                tempMp3.writeBytes(fuck)
//
//                asyncPlayer.play(this@MainActivity, Uri.fromFile(tempMp3),false, AudioManager.STREAM_MUSIC)
            }catch (e:Exception){

            }



        }








        val swipeContainer = binding.fuck
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener {
            Log.e("fuck","fuck")
        }

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);



    }



    fun upload(view: View) {
        val swipeContainer = binding.fuck
        swipeContainer.setRefreshing(false)
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), RequestSinglePhoto
        )



    }



}