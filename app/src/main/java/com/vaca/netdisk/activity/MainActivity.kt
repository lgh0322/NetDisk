package com.vaca.netdisk.activity

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.vaca.netdisk.MainApplication
import com.vaca.netdisk.R
import com.vaca.netdisk.databinding.ActivityMainBinding
import com.vaca.netdisk.net.NetCmd
import com.vaca.netdisk.utils.PathUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Thread.sleep


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val RequestSinglePhoto = 2
    val dataScope = CoroutineScope(Dispatchers.IO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PathUtil.initVar(this)












        val swipeContainer = binding.fuck
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener {

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
//        startActivityForResult(
//            Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            ), RequestSinglePhoto
//        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestSinglePhoto && resultCode == RESULT_OK && null != data) {
            try {
                val selectedImage = data.data
                val filePath: String = MainApplication.fileUtils.getPath(selectedImage)
                dataScope.launch {
                    NetCmd.uploadFile(File(filePath))
                }

            }catch (e:java.lang.Exception){

            }
        }
    }


}