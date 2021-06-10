package com.vaca.netdisk

import android.app.Application
import com.vaca.netdisk.utils.FileUtils

class MainApplication :Application(){
    companion object{
        lateinit var fileUtils:FileUtils
    }
    override fun onCreate() {
        super.onCreate()
        fileUtils = FileUtils(this)
    }
}