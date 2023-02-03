package com.rndetector

import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast

class ScreenrecordDetectionDelegate(private val context: Context, val listener: ScreenrecordDetectionListener) {

    private var contentObserver: ContentObserver? = null

    fun start() {
        if (contentObserver == null) {
            contentObserver = context.contentResolver.registerObserver()
        }
    }

    fun stop() {
        contentObserver?.let { context.contentResolver.unregisterContentObserver(it) }
        contentObserver = null
    }

    private fun queryScreenshots(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            queryRelativeDataColumn(uri)
        } else {
            queryDataColumn(uri)
        }
    }

    private fun queryDataColumn(uri: Uri) {
        val projection = arrayOf(
            MediaStore.Video.Media.DATA
        )
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            while (cursor.moveToNext()) {
                val path = cursor.getString(dataColumn)
                if (path.contains("record", true)) {
                    // do something
                    // Toast.makeText(context, path, Toast.LENGTH_LONG).show()
                    onScreenRecorded(path!!)
                }
            }
        }
    }

    private fun queryRelativeDataColumn(uri: Uri) {
        val projection = arrayOf(
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.RELATIVE_PATH
        )
        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val relativePathColumn =
                cursor.getColumnIndex(MediaStore.Video.Media.RELATIVE_PATH)
            val displayNameColumn =
                cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val name = cursor.getString(displayNameColumn)
                val relativePath = cursor.getString(relativePathColumn)
                if (name.contains("record", true) or
                    relativePath.contains("record", true)
                ) {
                    // do something
                    // Toast.makeText(context, name, Toast.LENGTH_LONG).show()
                    onScreenRecorded(name!!)
                }
            }
        }
    }

    private fun ContentResolver.registerObserver(): ContentObserver {
        val contentObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                super.onChange(selfChange, uri)
                uri?.let { queryScreenshots(it) }
            }
        }
        registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, contentObserver)
        return contentObserver
    }

    private fun onScreenRecorded(path: String) {
        listener.onScreenRecorded(path)
    }
}

interface ScreenrecordDetectionListener {
    fun onScreenRecorded(path: String)
}