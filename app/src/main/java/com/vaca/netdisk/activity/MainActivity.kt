package com.vaca.netdisk.activity

import android.app.Activity
import android.content.Intent
import android.media.AsyncPlayer
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.coroutines.withContext
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


        binding.ga.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                dataScope.launch {
                    try {
                        val gg=NetCmd.uploadFile3(binding.ga.text.toString())!!
                        withContext(Dispatchers.Main){
                            binding.hao.text=gg
                        }
                    }catch (e:Exception){

                    }
                }
            }

        })

    }





}