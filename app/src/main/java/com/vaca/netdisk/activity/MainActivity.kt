package com.vaca.netdisk.activity

import android.app.Activity
import android.content.Intent
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
import java.io.File
import java.io.IOException


class MainActivity : AppCompatActivity() {












    private val REQUEST_CODE_SELECT_IMG = 91
    private val MAX_SELECT_COUNT = 20
    val uploadFuck=MutableLiveData<Int>()

    lateinit var binding: ActivityMainBinding
    var uploadPop:UploadPop?=null
    private val RequestSinglePhoto = 2
    val dataScope = CoroutineScope(Dispatchers.IO)








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PathUtil.initVar(this)

        uploadFuck.observe(this,{
            uploadPop?.step2(it)
        })



        dataScope.launch {
            NetCmd.getFile("http://192.168.5.101:3001/","fuck.png",object:NetCmd.OnDownloadListener{
                override fun onDownloadSuccess(filePath: String?) {
                  Log.e("fuck","fuckfuck")
                }

                override fun onDownloading(progress: Int) {

                }

                override fun onDownloadFailed() {

                }

            })
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestSinglePhoto   && resultCode == Activity.RESULT_OK && null != data) {
            try {
                val selectedImage = data.data
                val filePath: String = fileUtils.getPath(selectedImage)

                uploadPop=UploadPop(this,object:UploadPop.ReceiveInfo{
                    override fun receive(s: Boolean) {
                        dataScope.launch {

                            try {
                                NetCmd.uploadFile(File(filePath),object:UploadProgressListener{
                                    override fun onProgress(len: Long, current: Int) {
                                        uploadFuck.postValue((current.toDouble()/len.toDouble()*100).toInt())
                                    }

                                })
                            }catch (e:Exception){

                            }



                        }
                    }

                },filePath)
                uploadPop?.showAtLocation(binding.root,Gravity.CENTER,0,0)




//                val selectedImage = data.data
//                val filePath: String = MainApplication.fileUtils.getPath(selectedImage)


            }catch (e:java.lang.Exception){

            }
        }
    }


}