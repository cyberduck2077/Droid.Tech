package ru.droid.tech.base.extension

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.droid.tech.BuildConfig
import ru.droid.tech.R
import ru.data.common.models.logger.LogCustom.logD
import ru.data.common.models.res.TextApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DEFAULT_CHANNEL_ID_FCM_DOWNLOAD = "fcm_default_channel_download"
fun patchFromPictures(fileName: String): File {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        TextApp.FOLDER_NAME)
    directory.mkdirs()
    return File(directory, fileName)
}

fun getFileMimeType(fileName: String): String? {
    val fileExtension = MimeTypeMap.getFileExtensionFromUrl(fileName)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
}

@Throws(IOException::class)
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", LocaleRu).format(Date())
    val storageDir = cacheDir
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        deleteOnExit()
    }
}

private val LocaleRu = Locale("ru", "RU")

fun Context.makeUri() = this.createImageFile().getUriForFile(this)

fun File.getUriForFile(context: Context): Uri {
    return FileProvider.getUriForFile(context,
        "${context.applicationInfo.packageName}.fileprovider",
        this)
}

fun downloadFromUrlExt(context: Context, link: String) {

    val nameFileDownload = link.substringAfterLast("/")
    val fillNamePatch = patchFromPictures(nameFileDownload)

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse(fillNamePatch.absolutePath), getFileMimeType(nameFileDownload))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    val pendingIntent = PendingIntent.getActivity(context, 101, intent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

    val builder: NotificationCompat.Builder =
        NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID_FCM_DOWNLOAD)
            .setSmallIcon(R.raw.ic_download)
            .setContentTitle(nameFileDownload)
            .setAutoCancel(true)
            .setVibrate(null)
            .setSound(null)
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)

    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notificationChannel = NotificationChannel(DEFAULT_CHANNEL_ID_FCM_DOWNLOAD,
        BuildConfig.APPLICATION_ID,
        NotificationManager.IMPORTANCE_HIGH)

    notificationManager.createNotificationChannel(notificationChannel)
    CoroutineScope(Dispatchers.Main).launch {
        withContext(Dispatchers.IO){
            try {
                val connection = URL(link).openConnection() as? HttpURLConnection
                connection?.let {
                    it.connect()
                    val inputStream = it.inputStream
                    val fileOutputStream = FileOutputStream(fillNamePatch)
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    var totalBytesRead: Long = 0
                    val totalFileSize = connection.contentLength.toLong()
                    var progress: Int

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                        totalBytesRead += bytesRead.toLong()
                        progress = ((totalBytesRead * 100) / totalFileSize).toInt()
                        builder.setProgress(100, progress, false)
                        notificationManager.notify(101, builder.build())
                    }

                    fileOutputStream.flush()
                    fileOutputStream.close()
                    inputStream.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        builder.setProgress(0, 0, false)
        notificationManager.notify(101, builder.build())
    }
}


fun getPhotoLocationFromUri(context: Context, photoUri: Uri) {
    try {
        val inputStream = context.contentResolver.openInputStream(photoUri)
        inputStream?.use {
            val exifInterface = ExifInterface(it)

            val latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
            val latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
            val longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
            val longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)
            val tagAperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE)
            val tagApertureValue = exifInterface.getAttribute(ExifInterface.TAG_APERTURE_VALUE)
            val tagArtist = exifInterface.getAttribute(ExifInterface.TAG_ARTIST)
            val tagBitsPerSample = exifInterface.getAttribute(ExifInterface.TAG_BITS_PER_SAMPLE)
            val tagBrightnessValue = exifInterface.getAttribute(ExifInterface.TAG_BRIGHTNESS_VALUE)
            val tagCfaPattern = exifInterface.getAttribute(ExifInterface.TAG_CFA_PATTERN)
            val tagColorSpace = exifInterface.getAttribute(ExifInterface.TAG_COLOR_SPACE)
            val tagComponentsConfiguration = exifInterface.getAttribute(ExifInterface.TAG_COMPONENTS_CONFIGURATION)
            val tagCompressedBitsPerPixel = exifInterface.getAttribute(ExifInterface.TAG_COMPRESSED_BITS_PER_PIXEL)
            val tagCompression = exifInterface.getAttribute(ExifInterface.TAG_COMPRESSION)
            val tagContrast = exifInterface.getAttribute(ExifInterface.TAG_CONTRAST)
            val tagCopyright = exifInterface.getAttribute(ExifInterface.TAG_COPYRIGHT)
            val tagCustomRendered = exifInterface.getAttribute(ExifInterface.TAG_CUSTOM_RENDERED)
            val tagDatetime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME)
            val tagDatetimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
            val tagDatetimeOriginal = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
            val tagDefaultCropSize = exifInterface.getAttribute(ExifInterface.TAG_DEFAULT_CROP_SIZE)
            val tagDeviceSettingDescription = exifInterface.getAttribute(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION)
            val tagDigitalZoomRatio = exifInterface.getAttribute(ExifInterface.TAG_DIGITAL_ZOOM_RATIO)


            logD("getPhotoLocationFromUri: latitude = $latitude")
            logD("getPhotoLocationFromUri: longitude = $longitude")
            logD("getPhotoLocationFromUri: latitudeRef = $latitudeRef")
            logD("getPhotoLocationFromUri: longitudeRef = $longitudeRef")
            logD("getPhotoLocationFromUri: tagAperture = $tagAperture")
            logD("getPhotoLocationFromUri: tagApertureValue = $tagApertureValue")
            logD("getPhotoLocationFromUri: tagArtist = $tagArtist")
            logD("getPhotoLocationFromUri: tagBitsPerSample = $tagBitsPerSample")
            logD("getPhotoLocationFromUri: tagBrightnessValue = $tagBrightnessValue")
            logD("getPhotoLocationFromUri: tagCfaPattern = $tagCfaPattern")
            logD("getPhotoLocationFromUri: tagColorSpace = $tagColorSpace")
            logD("getPhotoLocationFromUri: tagComponentsConfiguration = $tagComponentsConfiguration")
            logD("getPhotoLocationFromUri: tagCompressedBitsPerPixel = $tagCompressedBitsPerPixel")
            logD("getPhotoLocationFromUri: tagCompression = $tagCompression")
            logD("getPhotoLocationFromUri: tagContrast = $tagContrast")
            logD("getPhotoLocationFromUri: tagCopyright = $tagCopyright")
            logD("getPhotoLocationFromUri: tagCustomRendered = $tagCustomRendered")
            logD("getPhotoLocationFromUri: tagDatetime = $tagDatetime")
            logD("getPhotoLocationFromUri: tagDatetimeDigitized = $tagDatetimeDigitized")
            logD("getPhotoLocationFromUri: tagDatetimeOriginal = $tagDatetimeOriginal")
            logD("getPhotoLocationFromUri: tagDefaultCropSize = $tagDefaultCropSize")
            logD("getPhotoLocationFromUri: tagDeviceSettingDescription = $tagDeviceSettingDescription")
            logD("getPhotoLocationFromUri: tagDigitalZoomRatio = $tagDigitalZoomRatio")

            if (latitude != null && longitude != null && latitudeRef != null && longitudeRef != null) {
                val lat = convertToDegree(latitude)
                val lon = convertToDegree(longitude)

                val latResult = if (latitudeRef == "N") lat else -lat
                val lonResult = if (longitudeRef == "E") lon else -lon

                logD("Latitude: $latResult")
                logD("Longitude: $lonResult")
            } else {
                println("Location not available.")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun convertToDegree(coordinate: String): Float {
    val parts = coordinate.split(",").map { it.trim() }
    val degrees = parts[0].toDouble()
    val minutes = parts[1].toDouble()
    val seconds = parts[2].toDouble()

    return (degrees + minutes / 60 + seconds / 3600).toFloat()
}
