package com.vaca.netdisk.pop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vaca.netdisk.R


@SuppressLint("ClickableViewAccessibility")
class UploadPop(mContext: Context, r: ReceiveInfo, url:String) : PopupWindow() {
    private val mContext: Context? = null

    interface ReceiveInfo {
        fun receive(s: Boolean)
    }


    init {
        val view = LayoutInflater.from(mContext).inflate(R.layout.pop_upload, null)
        isOutsideTouchable = true
        contentView = view
        height = RelativeLayout.LayoutParams.MATCH_PARENT
        width = RelativeLayout.LayoutParams.MATCH_PARENT
        isFocusable = true
        val dw = ColorDrawable(-0x80000000)
        setBackgroundDrawable(dw)
        this.animationStyle = R.style.take_photo_anim


        val image:ImageView=view.findViewById(R.id.main)
        Glide.with(mContext)
            .load(url)
            .into(image)




        val close: ImageView = view.findViewById(R.id.close)
        close.setOnClickListener {
            dismiss()
        }



        val cancel: TextView = view.findViewById(R.id.cancel)
        cancel.setOnClickListener {

            dismiss()
        }

        val goAhead: TextView = view.findViewById(R.id.goAhead)
        goAhead.setOnClickListener {
            r.receive(true)
            dismiss()
        }

    }


}