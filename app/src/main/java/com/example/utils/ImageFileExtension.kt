package com.example.utils

import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.memoriae.ui.activities.AddEditMemory

class ImageFileExtension(private val activity:AddEditMemory,private val uri: Uri?){
    fun getFileExtension():String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}