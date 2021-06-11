package com.vaca.netdisk.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.vaca.netdisk.R


class PermissionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        requestPermission()
    }

    private val permissionRequestCode = 521
    private fun checkP(p: String): Boolean {
        return ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val ps: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (!checkP(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, ps, permissionRequestCode)
            return
        } else {
            initA()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            permissionRequestCode -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initA()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            }
            else -> {
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun initA() {

            initB()

    }

    fun initB() {
        startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }



    private val REQUEST_READ = 223

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_READ) {
            initB()
        }
    }
}