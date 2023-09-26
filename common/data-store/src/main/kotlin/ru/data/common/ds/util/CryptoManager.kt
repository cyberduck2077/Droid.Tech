package ru.data.common.ds.util

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ru.data.common.ds.util.Base64.decodeFromBase64
import ru.data.common.ds.util.Base64.encodeToBase64
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


internal class CryptoManager(private val file: File) {

    companion object {
        private const val ALGORITHM = "AES"
        private const val BLOCK_MODE = "CBC"
        private const val PADDING = "PKCS7Padding"
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2With${"HmacSHA1"}"
        private const val MAX_LIST_SIZE = 30
    }

    @Serializable
    private data class CryptoCase(val list: Map<Int, String>? = null)

    private class CryptoCasesSerializer() : Serializer<CryptoCase> {
        override val defaultValue: CryptoCase = CryptoCase()
        override suspend fun readFrom(input: InputStream): CryptoCase {
            return try {
                val text = input.reader().use { reader ->
                    reader.readText()
                }
                Json.decodeFromString(
                    deserializer = CryptoCase.serializer(),
                    string = text
                )
            } catch (e: SerializationException) {
                e.printStackTrace()
                defaultValue
            }
        }

        override suspend fun writeTo(data: CryptoCase, output: OutputStream) {
            try {

                val json = Json.encodeToString(
                    serializer = CryptoCase.serializer(),
                    value = data
                ).encodeToByteArray()

                output.also {
                    it.write(json)
                }

            } catch (e: SerializationException) {
                e.printStackTrace()
            }
        }
    }


    private val dataStore = DataStoreFactory.create(
        produceFile = { file },
        serializer = CryptoCasesSerializer()
    )

//    private val secretKey = getDigitKey(2, 13)
//    private val codeSalt = getDigitKey(3, 9)
//    private val codeForeIV = getDigitKey(4)

    private val secretKey = 13
    private val codeSalt = 9
    private val codeForeIV = 25

    private fun getDigitKey(numb: Int, plusNumb: Int = 0): Int {
        val timeNuw = System.currentTimeMillis().toString()
        val numbKey = (timeNuw.getOrNull(numb)?.digitToInt() ?: numb) + plusNumb
        if (numbKey > MAX_LIST_SIZE) return numb
        return numbKey.coerceIn(0, MAX_LIST_SIZE)
    }

    private fun getMapKey() = runBlocking { dataStore.data.first().list ?: mapOf() }

    private fun generate(): ByteArray {
        val random = SecureRandom()
        val genByteArray = ByteArray(16)
        random.nextBytes(genByteArray)
        return genByteArray
    }

    private fun generateMapKey(): Map<Int, String> {
        val editorMap: Map<Int, String> = (0..MAX_LIST_SIZE).associateWith {
            generate().encodeToBase64()
        }
        runBlocking {
            dataStore.updateData {
                it.copy(list = editorMap)
            }
        }

        return getMapKey()
    }

    private fun getSecretKey(secretKey: String, codeStr: String): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM)
        val spec =
            PBEKeySpec(secretKey.toCharArray(), codeStr.decodeFromBase64(), 10000, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, ALGORITHM)
    }

    private fun getIV(codeForeIV: String): IvParameterSpec {
        return IvParameterSpec(codeForeIV.decodeFromBase64())
    }

   internal fun encrypt(strToEncrypt: String) = try {
        val editorMap = generateMapKey()
        val key = editorMap.getOrElse(secretKey) { "" }
        val salt = editorMap.getOrElse(codeSalt) { "" }
        val iv = editorMap.getOrElse(codeForeIV) { "" }

        val encryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getSecretKey(key, salt), getIV(iv))
        }
        encryptCipher.doFinal(strToEncrypt.toByteArray(Charsets.UTF_8)).encodeToBase64()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    internal  fun decrypt(strToDecrypt: String) = try {
        val loadedMap = getMapKey()
        val key = loadedMap.getOrElse(secretKey) { "" }
        val salt = loadedMap.getOrElse(codeSalt) { "" }
        val iv = loadedMap.getOrElse(codeForeIV) { "" }
        val decryptCipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getSecretKey(key, salt),
                getIV(iv)
            )
        }
        String(decryptCipher.doFinal(strToDecrypt.decodeFromBase64()))
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
