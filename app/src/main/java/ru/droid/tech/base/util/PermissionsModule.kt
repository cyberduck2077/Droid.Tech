package ru.droid.tech.base.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.droid.tech.BuildConfig
import ru.data.common.models.logger.LogCustom.logD
import ru.data.common.models.logger.LogCustom.logE
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random


class PermissionsModule(val context: Context) {
    private fun checkPermission(namePermission: String): Int {
        val inPermission = ContextCompat.checkSelfPermission(context, namePermission)
        logIRRealiseModule("permission checkPermission", namePermission, inPermission == GRANTED)
        return inPermission
    }

    /**
     * Get Permissions
     * */
    private val permissionCamera by lazy { checkPermission(CAMERA) }
    private val permissionAccessCoarseLocation by lazy { checkPermission(ACCESS_COARSE_LOCATION) }
    private val permissionAccessFineLocation by lazy { checkPermission(ACCESS_FINE_LOCATION) }
    private val permissionReadExternalStorage by lazy { checkPermission(READ_EXTERNAL_STORAGE) }
    private val permissionWriteExternalStorage by lazy { checkPermission(WRITE_EXTERNAL_STORAGE) }
    private val permissionReadMediaImages by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) checkPermission(READ_MEDIA_IMAGES) else GRANTED
    }
    private val permissionReadMediaVideo by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) checkPermission(READ_MEDIA_VIDEO) else GRANTED
    }
    private val permissionReadMediaAudio by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) checkPermission(READ_MEDIA_AUDIO) else GRANTED
    }

    /**
     * Check Permissions
     * */
    fun grantedCamera(): Boolean = permissionCamera == GRANTED
    fun grantedLocationCoarse(): Boolean = permissionAccessCoarseLocation == GRANTED
    fun grantedLocationFine(): Boolean = permissionAccessFineLocation == GRANTED
    fun grantedReadStorage(): Boolean = permissionReadExternalStorage == GRANTED
    fun grantedWriteStorage(): Boolean = permissionWriteExternalStorage == GRANTED

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun grantedReadMediaImages(): Boolean = permissionReadMediaImages == GRANTED

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun grantedReadMediaVideo(): Boolean = permissionReadMediaVideo == GRANTED

    fun listPermissionsNeededCameraImagesReadWriteStorage(): Array<String> {
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        listPermissionsNeeded.add(CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listPermissionsNeeded.add(READ_MEDIA_IMAGES)
            listPermissionsNeeded.add(READ_MEDIA_VIDEO)
        } else {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE)
        }
        return listPermissionsNeeded.toTypedArray()
    }


    fun listPermissionsNeededReadWriteStorage(): Array<String> {
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listPermissionsNeeded.add(READ_MEDIA_IMAGES)
            listPermissionsNeeded.add(READ_MEDIA_VIDEO)
        } else {
            listPermissionsNeeded.add(READ_EXTERNAL_STORAGE)
            listPermissionsNeeded.add(WRITE_EXTERNAL_STORAGE)
        }
        return listPermissionsNeeded.toTypedArray()
    }

    fun checkCameraImagesReadWriteStorage(): Boolean {
        val listPermissionsNeeded: MutableList<Int> = ArrayList()
        listPermissionsNeeded.add(permissionCamera)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listPermissionsNeeded.add(permissionReadMediaImages)
            listPermissionsNeeded.add(permissionReadMediaVideo)
        } else {
            listPermissionsNeeded.add(permissionReadExternalStorage)
            listPermissionsNeeded.add(permissionWriteExternalStorage)
        }
        return listPermissionsNeeded.contains(GRANTED)
    }

    fun checkReadWriteStorage(): Boolean {
        val listPermissionsNeeded: MutableList<Int> = ArrayList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listPermissionsNeeded.add(permissionReadMediaImages)
            listPermissionsNeeded.add(permissionReadMediaVideo)
        } else {
            listPermissionsNeeded.add(permissionReadExternalStorage)
            listPermissionsNeeded.add(permissionWriteExternalStorage)
        }
        return listPermissionsNeeded.contains(GRANTED)
    }

    fun listPermissionsNeededLocation(): Array<String> =
        arrayListOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION).toTypedArray()


    companion object {
        private const val GRANTED = PackageManager.PERMISSION_GRANTED
        private const val CAMERA = Manifest.permission.CAMERA
        private const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        private const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val READ_MEDIA_VIDEO = Manifest.permission.READ_MEDIA_VIDEO

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val READ_MEDIA_AUDIO = Manifest.permission.READ_MEDIA_AUDIO

        @Composable
        fun SetLocation(address: (List<Address>) -> Unit) {
            val context = LocalContext.current
            PermissionCoarseLocation { permitted ->
                if (permitted) {
                    getLocationServices(context) {
                        CoroutineScope(Dispatchers.Main).launch {
                            address.invoke(it)
                        }
                    }
                }
            }
        }

        @Composable
        fun permissionLauncher(returnResult: (Boolean) -> Unit) =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                returnResult(isGranted)
            }

        @Composable
        fun launchPermissionMultiple(response: (Boolean) -> Unit): ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>> {
            return rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    logDModule("permissionMultiple", "${it.key} = ${it.value}")
                }
                response.invoke(permissions.all { (_, permissionValue) -> permissionValue == true })
            }
        }

        @Composable
        fun launchPermissionCameraAndGallery(response: (Boolean) -> Unit): () -> Unit {
            val context = LocalContext.current
            val permissionsModule = remember { PermissionsModule(context) }
            val launch = launchPermissionMultiple(response)
            return {
                if (permissionsModule.checkCameraImagesReadWriteStorage()) {
                    response.invoke(true)
                } else {
                    launch.launch(permissionsModule.listPermissionsNeededCameraImagesReadWriteStorage())
                }
            }

        }

        @Composable
        fun PermissionCoarseLocation(returnResult: (Boolean) -> Unit) {
            val context = LocalContext.current
            val permissionsModule = remember { PermissionsModule(context) }
            val permission = launchPermissionMultiple { isGranted -> returnResult(isGranted) }
            LaunchedEffect(key1 = Unit, block = {
                val isPermission =
                    permissionsModule.grantedLocationCoarse() && permissionsModule.grantedLocationFine()
                if (!isPermission) {
                    permission.launch(permissionsModule.listPermissionsNeededLocation())
                } else {
                    returnResult(true)
                }
            })
        }


        @Composable
        fun getListImage(
            listImage: (List<Uri>) -> Unit,
        ): () -> Unit {
            val context = LocalContext.current
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/* video/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            val pickMultipleMedia =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { uris ->
                    if (uris.resultCode == Activity.RESULT_OK) {
                        val data = uris.data
                        val selectedImages = mutableListOf<Uri>()
                        if (data?.clipData != null) {
                            for (i in 0 until data.clipData!!.itemCount) {
                                val uri = data.clipData!!.getItemAt(i).uri
                                val realUri = context.getFileRealPatch(uri).toUri()
                                selectedImages.add(realUri)
                            }
                        } else if (data?.data != null) {
                            val uri = data.data!!
                            val realUri = context.getFileRealPatch(uri).toUri()
                            selectedImages.add(realUri)
                        }
                        listImage(selectedImages)
                    }
                }
            return {
                try {
                    pickMultipleMedia.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @Composable
        fun getListImagePickMultiple(
            listImage: (List<Uri>) -> Unit,
        ): () -> Unit {
            val context = LocalContext.current
            val intent =
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
            val pickMultipleMedia =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.PickMultipleVisualMedia()
                ) { uris ->
                    val mediaUris = mutableListOf<Uri>()
                    uris.forEach { uri ->
                        val file = context.outputFileNew(uri) ?: run {
                            logIRRealiseModule("Cannot save the image!")
                            return@rememberLauncherForActivityResult
                        }
                        mediaUris.add(file.toUri())
                    }

                    listImage(mediaUris)
                }
            return {
                try {
                    pickMultipleMedia.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @Composable
        fun getListImageCursor(
            listImage: (List<Uri>) -> Unit
        ): () -> Unit {
            val context = LocalContext.current
            val permissionsModule = remember { PermissionsModule(context) }
            val getImage: (Boolean) -> Unit = remember {
                {
                    if (it) {
                        val imageUris = mutableListOf<Uri>()
                        val projection = arrayOf(MediaStore.Images.Media._ID)
                        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

                        val cursor =
                            context.contentResolver.query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                projection,
                                null,
                                null,
                                sortOrder
                            )

                        cursor?.use {
                            val columnIndex =
                                cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                            while (cursor.moveToNext()) {
                                val imageId = cursor.getLong(columnIndex)
                                val contentUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        imageId
                                    )
                                imageUris.add(contentUri)
                            }
                        }

                        listImage.invoke(imageUris)
                    }
                }
            }

            val launch = launchPermissionMultiple(getImage)
            return {
                if (permissionsModule.checkReadWriteStorage()) {
                    getImage.invoke(true)
                } else {
                    launch.launch(permissionsModule.listPermissionsNeededReadWriteStorage())
                }
            }
        }


        @Composable
        fun getListVideo(
            listVideo: (List<Uri>) -> Unit
        ): () -> Unit {
            val context = LocalContext.current
            val permissionsModule = remember { PermissionsModule(context) }
            val getVideo: (Boolean) -> Unit = remember {
                {
                    if (it) {
                        val videoUris = mutableListOf<Uri>()
                        val projection = arrayOf(MediaStore.Video.Media._ID)
                        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

                        val cursor =
                            context.contentResolver.query(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                projection,
                                null,
                                null,
                                sortOrder
                            )

                        cursor?.use {
                            val columnIndex =
                                cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                            while (cursor.moveToNext()) {
                                val videoId = cursor.getLong(columnIndex)
                                val contentUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                        videoId
                                    )
                                videoUris.add(contentUri)
                            }
                        }
                        listVideo.invoke(videoUris)
                    }
                }
            }

            val launch = launchPermissionMultiple(getVideo)
            return {
                if (permissionsModule.checkReadWriteStorage()) {
                    getVideo.invoke(true)
                } else {
                    launch.launch(permissionsModule.listPermissionsNeededReadWriteStorage())
                }
            }
        }

        @Composable
        fun galleryLauncher(
            uploadPhoto: (Uri) -> Unit
        ): () -> Unit {
            val context = LocalContext.current
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val launchRun =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val uri = result.data?.data ?: run {
                            logIRRealiseModule("Cannot save the image!")
                            return@rememberLauncherForActivityResult
                        }
                        val file = context.outputFile(uri) ?: run {
                            logIRRealiseModule("Cannot save the image!")
                            return@rememberLauncherForActivityResult
                        }
                        uploadPhoto(file.toUri())
                    }
                }
            return {
                try {
                    launchRun.launch(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @Composable
        fun cameraLauncher(
            uploadPhoto: (Uri) -> Unit
        ): () -> Unit {
            val context = LocalContext.current
            val imageUri by remember { mutableStateOf(context.makeUriModule()) }
            val launchRun =
                rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                    if (success) {
                        context.outputFile(imageUri)?.let { new ->
                            uploadPhoto(new.toUri())
//                            getPhotoLocationFromUri(context, new.toUri())
                        } ?: run {
                            logEModule("Cannot save the image!")
                        }
                    } else {
                        logEModule("Cannot save the image!")
                    }
                }
            return {
                try {
                    launchRun.launch(imageUri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun getLocationServices(context: Context, localGeo: (List<Address>) -> Unit): Boolean {
    val locationServices = LocationServices.getFusedLocationProviderClient(context)
     if (PermissionsModule(context).grantedLocationFine() && PermissionsModule(
            context
        ).grantedLocationCoarse()) {
        try {
            locationServices.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    getGeoLocationAddress(latitude, longitude, context) {
                        localGeo.invoke(it)
                    }
                    logD("location ${latitude}: $longitude")
                }
                logD("location $location")
            }.addOnFailureListener {
                logE(it.stackTraceToString())
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    return false
}

fun getGeoLocationAddress(
    latitude: Double, longitude: Double, context: Context, localGeo: (List<Address>) -> Unit
) {
    val geocoder = Geocoder(context, LocaleRuModule)
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1) { addresses ->
                localGeo.invoke(addresses)
            }
        } else {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1) ?: listOf()
            localGeo.invoke(addresses)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun Context.makeUriModule() = this.createImageFileModule().getUriForFileModule(this)

@SuppressLint("Recycle")
private fun Context.outputFile(uri: Uri): File? {
    val input = this.contentResolver.openInputStream(uri) ?: return null
    val dataName =
        SimpleDateFormat("yyyyMMdd_HHmmss", LocaleRuModule).format(System.currentTimeMillis())
    val outputFile = this.filesDir.resolve("${dataName}_new_picture.jpg")
    input.copyTo(outputFile.outputStream())
    return outputFile
}

@SuppressLint("Recycle")
private fun Context.outputFileNew(uri: Uri): File? {
    val input = this.contentResolver.openInputStream(uri) ?: return null
    val random = Random()
    val dataName =
        SimpleDateFormat("yyyyMMdd_HHmmss", LocaleRuModule).format(System.currentTimeMillis())
    val randomNumber = random.nextInt()
    val outputFile = this.filesDir.resolve("rand_num_${randomNumber}${dataName}_new_picture.jpg")
    input.copyTo(outputFile.outputStream())
    return outputFile
}


@Throws(IOException::class)
private fun Context.createImageFileModule(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", LocaleRuModule).format(Date())
    val storageDir = cacheDir
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        deleteOnExit()
    }
}

private fun File.getUriForFileModule(context: Context): Uri {
    return FileProvider.getUriForFile(
        context,
        "${context.applicationInfo.packageName}.fileprovider",
        this
    )
}


private fun logEModule(vararg any: Any?) {
    if (!SHOW_LOG_MODULE) return
    Log.e(mainStrModule, any.joinToString(", ", "{", "}"))
}

private fun logIRRealiseModule(vararg any: Any?) {
    Log.i(mainStrModule, any.joinToString(", ", "{", "}"))
}

private fun logDModule(vararg any: Any?) {
    if (!SHOW_LOG_MODULE) return
    Log.d(mainStrModule, any.joinToString(", ", "{", "}"))
}

private const val mainStrModule = "TECH ${BuildConfig.APPLICATION_ID}"
private val SHOW_LOG_MODULE = BuildConfig.DEBUG
private val LocaleRuModule = Locale("ru", "RU")

