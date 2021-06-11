package com.vaca.netdisk.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.scrat.app.selectorlibrary.ImageSelector
import com.vaca.netdisk.databinding.ActivityMainBinding
import com.vaca.netdisk.net.NetCmd
import com.vaca.netdisk.net.UploadProgressListener
import com.vaca.netdisk.pop.UploadPop
import com.vaca.netdisk.utils.PathUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File


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
//        val swipeContainer = binding.fuck
//        swipeContainer.setRefreshing(false)
//        startActivityForResult(
//            Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//            ), RequestSinglePhoto
//        )

        ImageSelector.show(
            this,
            REQUEST_CODE_SELECT_IMG,
            MAX_SELECT_COUNT
        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            try {
                val paths = ImageSelector.getImagePaths(data) ?: return
                if (paths.isEmpty()) return

                val filePath=paths[0]

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