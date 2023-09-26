package ru.droid.tech.base.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Dimension
import coil.size.Size
import okhttp3.internal.wait
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatform.getKoin
import ru.droid.tech.base.BaseModel
import ru.data.common.models.local.maps.UserUI
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Objects
import java.util.UUID
import kotlin.jvm.internal.ClassBasedDeclarationContainer
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@Composable
fun rememberNavigationBarHeight() :Int{
    val des = LocalDensity.current
    val windowInsetsNavigationBars = WindowInsets.navigationBars
    return remember { derivedStateOf { windowInsetsNavigationBars.getBottom(des) } }.value
}

@Composable
fun rememberImeHeight() :Int{
    val des = LocalDensity.current
    val windowInsetsIme = WindowInsets.ime
    return remember { derivedStateOf { windowInsetsIme.getBottom(des) } }.value
}


@Composable
fun rememberStatusBarHeight() :Int{
    val des = LocalDensity.current
    val windowInsetsStatusBars = WindowInsets.statusBars
    return remember { derivedStateOf { windowInsetsStatusBars.getTop(des) } }.value
}




fun <T> modifyList(list: List<T>, newItem: T): List<T> {
    val listMut = list.toMutableList()
    if (listMut.contains(newItem)) {
        listMut.remove(newItem)
    } else {
        listMut.add(newItem)
    }
    return listMut
}


@Composable
inline fun <reified T : BaseModel> Screen.rememberModel(
    qualifier: Qualifier? = null,
    tag: String? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val navigator = LocalNavigator.currentOrThrow
    val model = rememberScreenModel(tag = tag ?: qualifier?.value) {
        getKoin().get<T>(qualifier, parameters)
    }
    LifeScreen(onResume = { model.initNavigator(navigator) })
    return model
}

@Suppress("UPPER_BOUND_VIOLATED")
val <T> KClass<T>.getQualifiedName: String
    @JvmName("getJavaClass")
    get() = ((this as ClassBasedDeclarationContainer).jClass).enclosingClass?.kotlin?.qualifiedName
        ?: UUID.randomUUID().toString()


@Composable
inline fun <T> rememberDerivedState(crossinline producer: @DisallowComposableCalls () -> T) =
    remember { derivedStateOf { producer() } }

@Composable
inline fun <T> rememberDerivedState(
    key1: Any?,
    crossinline producer: @DisallowComposableCalls () -> T
) =
    remember(key1) { derivedStateOf { producer() } }

@Composable
inline fun <T> rememberDerivedState(
    key1: Any?,
    key2: Any?,
    crossinline producer: @DisallowComposableCalls () -> T
) =
    remember(key1, key2) { derivedStateOf { producer() } }


@Composable
inline fun <T> rememberState(crossinline producer: @DisallowComposableCalls () -> T) =
    remember { mutableStateOf(producer()) }

@Composable
inline fun <T> rememberState(key1: Any?, crossinline producer: @DisallowComposableCalls () -> T) =
    remember(key1) { mutableStateOf(producer()) }

@Composable
inline fun <T> rememberState(
    key1: Any?,
    key2: Any?,
    crossinline producer: @DisallowComposableCalls () -> T
) =
    remember(key1, key2) { mutableStateOf(producer()) }

@Composable
inline fun <T> rememberState(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    crossinline producer: @DisallowComposableCalls () -> T
) =
    remember(key1, key2, key3) { mutableStateOf(producer()) }

@Composable
inline fun <T> rememberState(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    key4: Any?,
    crossinline producer: @DisallowComposableCalls () -> T
) =
    remember(key1, key2, key3, key4) { mutableStateOf(producer()) }

@Composable
fun BackPressHandler(
    backPressedDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
    onBackPressed: () -> Unit
) {
    var countRestart by remember { mutableStateOf(0) }
    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                currentOnBackPressed()
            }
        }
    }
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            if (event == Lifecycle.Event.ON_RESUME) countRestart++
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    LifeScreen(onResume = { countRestart++ })
    DisposableEffect(key1 = backPressedDispatcher, key2 = countRestart, effect = {
        backPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    })
}


@Composable
fun rememberImageRaw(@RawRes id: Int) = rememberAsyncImagePainter(
    model = ImageRequest.Builder(LocalContext.current)
        .apply {
            size(
                Size(
                    with(LocalDensity.current) {
                        LocalConfiguration.current.screenWidthDp.dp.roundToPx()
                    },
                    Dimension.Undefined
                )
            )
            decoderFactory(SvgDecoder.Factory())
            data(id)
            crossfade(false)
        }
        .build()
)


@Composable
fun LifeScreen(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
    onAny: () -> Unit = {},
) {
    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate()
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            Lifecycle.Event.ON_ANY -> onAny()
        }
    }
}


@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}


fun readJsonFromRaw(context: Context, @RawRes resourceId: Int): String {
    val inputStream = context.resources.openRawResource(resourceId)
    val json = try {
        inputStream.bufferedReader().use { it.readText() }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    } finally {
        inputStream.close()
    }
    return json
}


fun <T> List<T>.addUniqueElements(targetList: List<T>): List<T> {
    val targetListUnique = targetList.toMutableList()
    for (element in this) {
        if (!targetListUnique.contains(element)) {
            targetListUnique.add(element)
        }
    }
    return targetListUnique
}

fun <T> List<T>.removeElementsIfPresent(elementsToRemove: List<T>): List<T> {
    val resultList = mutableListOf<T>()
    for (element in this) {
        if (!elementsToRemove.contains(element)) {
            resultList.add(element)
        }
    }
    return resultList
}

fun getFileDocumentUtils(context: Context, documentUri: Uri): File {
    val inputStream = context.contentResolver?.openInputStream(documentUri)
    var file = File("")
    inputStream.use { input ->
        file =
            File(context.cacheDir, System.currentTimeMillis().toString() + ".jpeg")
        FileOutputStream(file).use { output ->
            val buffer =
                ByteArray(4 * 1024) // or other buffer size
            var read: Int = -1
            while (input?.read(buffer).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
    }
    return file
}


fun Context.getFileRealPatch(documentUri: Uri): File {
    val inputStream = contentResolver?.openInputStream(documentUri)
//    val tika = Tika()
    val file: File
    inputStream.use { input ->
        val extension =  getExtensionFromUri(this,documentUri)
        file = File(cacheDir, System.currentTimeMillis().toString() + ".${extension ?: "jpeg"}")
        FileOutputStream(file).use { output ->
            val buffer = ByteArray(4 * 1024)
            var read: Int = -1
            while (input?.read(buffer).also {
                    if (it != null) {
                        read = it
                    }
                } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
    }

    inputStream?.close()
    return file
}


fun getExtensionFromUri(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst()) {
            val fileName = cursor.getString(nameIndex)
            val dotIndex = fileName.lastIndexOf('.')
            if (dotIndex >= 0) {
                cursor.close()
                return fileName.substring(dotIndex + 1)
            }
        }
    }
    cursor?.close()
    return null
}