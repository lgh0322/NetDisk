package com.vaca.netdisk

interface UploadProgressListener {
    fun onProgress(len:Long,current:Int)
}