package ru.droid.tech.base.common_composable

import android.app.Activity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import ru.droid.tech.base.util.BackPressHandler


@Composable
fun DialogBackPressExit() {
    val exitCheck = remember { mutableStateOf(false) }
    val activity = (LocalContext.current as? Activity)
    BackPressHandler { exitCheck.value = true }
    val onDismiss = { exitCheck.value = false }
    if (exitCheck.value) {
        Dialog(onDismissRequest = onDismiss) {
            DialogContentExit(
                onDismiss = onDismiss,
                onClickExit = { activity?.finish() }
            )
        }
    }
}
