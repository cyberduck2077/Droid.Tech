package ru.droid.tech.base.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun rememberOpenIntentUrl(onError: () -> Unit = {}): (String) -> Boolean {
    val uriHandler = LocalUriHandler.current
    return remember {
        { uri ->
            try {
                uriHandler.openUri(uri)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                onError.invoke()
                false
            }
        }
    }
}