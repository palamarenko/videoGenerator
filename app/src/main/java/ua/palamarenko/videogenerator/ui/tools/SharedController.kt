package ua.palamarenko.videogenerator.ui.tools

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import java.io.File


object SharedController {


    fun shareCachedFile(
        context: Context,
        file: File
    ): Boolean {
        val contentUri =
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        if (contentUri != null) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            // temporary grant permission for receiving app to read this file
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.type = "video/mp4"
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            try {
                context.startActivity(Intent.createChooser(shareIntent, "Shared"))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                return false
            }
        }
        return true
    }


    fun shareFile(
        context: Context,
        file: File
    ) {

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "video/mp4"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Share MP4"))
    }
}