package ua.palamarenko.videogenerator.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import org.koin.androidx.compose.koinViewModel
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun mainScreen(viewModel: GenerateVideoViewModel = koinViewModel()) {

    val progress by viewModel.progress.collectAsState()

    var text by remember { mutableStateOf("") }

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        viewModel
            .shared
            .collect { file -> if (file != null) shareVideo(file, context) }
    }

    Scaffold() {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            Arrangement.SpaceBetween,
            Alignment.CenterHorizontally
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.generateVideo(text)
                }
            ) {
                Text("Подтвердить")
            }
        }

        if (progress.show) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .clickable(onClick = {}),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    LinearProgressIndicator()
                    Text(text = progress.message + " (${progress.progress}%)")
                }

            }
        }
    }
}


private fun shareVideo(file: File, context: Context) {
    val uri = FileProvider.getUriForFile(
        context,
        "ua.palamarenko.videogenerator.fileprovider",
        file
    )
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "video/mp4"
        putExtra(Intent.EXTRA_STREAM, uri)
    }

    val chooser = Intent.createChooser(shareIntent, "Share File")

    val resInfoList: List<ResolveInfo> =
        context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

    for (resolveInfo in resInfoList) {
        val packageName = resolveInfo.activityInfo.packageName
        context.grantUriPermission(
            packageName,
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    }

    context.startActivity(chooser)
}
