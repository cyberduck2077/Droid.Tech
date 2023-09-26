package ru.droid.tech.base.common_composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import ru.droid.tech.base.res.DimApp
import ru.droid.tech.base.theme.LocalFixedInsets
import ru.data.common.models.res.TextApp
import ru.droid.tech.base.util.picker.DataPickerddmmyyy
import ru.droid.tech.base.util.rememberState

@Composable
fun DialogDataPickerddmmyyy(
    onDismissDialog: () -> Unit,
    onDataMillisSet: (Long) -> Unit,
    initTime: Long? = null
) {
    DialogBottomSheet(
        onDismiss = onDismissDialog
    ) { onDismiss ->
        var bethTime by rememberState(initTime) { initTime ?: System.currentTimeMillis() }
        val des = LocalDensity.current
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DimApp.screenPadding)
        ) {
            BoxSpacer()
            TextBodyLarge(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = TextApp.textBirthDay
            )
            DataPickerddmmyyy(
                modifier = Modifier.padding(horizontal = DimApp.screenPadding),
                startTimeMillis = bethTime,
                onDataChose = { bethTime = it }
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                ButtonAccentTextApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = onDismiss,
                    text = TextApp.titleCancel
                )
                BoxSpacer()
                ButtonAccentApp(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    onClick = {
                        onDataMillisSet.invoke(bethTime)
                        onDismiss.invoke()
                    },
                    text = TextApp.titleNext
                )
            }
            Box(modifier = Modifier.size(with(des) { LocalFixedInsets.current.navigationBars.toDp() }))
        }
    }

}