package com.vanced.manager.core.installer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.dezlum.codelabs.getjson.GetJson
import com.vanced.manager.core.base.BaseFragment
import com.vanced.manager.ui.MainActivity
import zlc.season.rxdownload4.file
import zlc.season.rxdownload4.task.Task
import zlc.season.rxdownload4.utils.getFileNameFromUrl

object MicrogInstaller {
    
    fun installMicrog(context: Context) {
        val apkUrl = GetJson().AsJSONObject("https://x1nto.github.io/VancedFiles/microg.json")
        val dwnldUrl = apkUrl.get("url").asString
        val task = context.filesDir?.path?.let {
            Task(
                url = dwnldUrl,
                saveName = getFileNameFromUrl(dwnldUrl),
                savePath = it
            )
        }
        val pn = context.packageName
        val apk = task?.file()
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context.let { FileProvider.getUriForFile(it, "$pn.provider", apk!!) }
            } else
                Uri.fromFile(apk)
        val mIntent = Intent(Intent.ACTION_VIEW)
        mIntent.setDataAndType(uri, "application/vnd.android.package-archive")
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        mIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        mIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true)
        startActivityForResult(
            MainActivity(),
            mIntent,
            BaseFragment.MICROG_INSTALL,
            null
        )
    }
    
}