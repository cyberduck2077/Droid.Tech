package ru.data.common.ds

import androidx.datastore.core.Serializer
import ru.data.common.ds.util.CryptoManager
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.data.common.models.local.LocalFileValue
import java.io.File
import java.io.InputStream
import java.io.OutputStream


internal class LocalFileSerializer(
    private val cryptoManager: CryptoManager
) : Serializer<LocalFileValue> {

    constructor(file: File) : this(CryptoManager(file))

    override val defaultValue: LocalFileValue get() = LocalFileValue()

    override suspend fun readFrom(input: InputStream): LocalFileValue {
        return try {
            val text = input.reader().use { reader -> reader.readText() }
            val decode = cryptoManager.decrypt(text) ?: run { return defaultValue }
            Json.decodeFromString(
                deserializer = LocalFileValue.serializer(),
                string = decode
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: LocalFileValue, output: OutputStream) {
        try {
            val jsonString = Json.encodeToString(
                serializer = LocalFileValue.serializer(),
                value = t
            )
            val encryptBytes = cryptoManager.encrypt(jsonString) ?: return
            output.also {
                it.write(encryptBytes.encodeToByteArray())
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
        }
    }
}
