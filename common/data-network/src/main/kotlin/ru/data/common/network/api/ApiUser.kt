package ru.data.common.network.api


import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import ru.data.common.models.local.maps.MimeTypeAttachment
import ru.data.common.models.network.NetworkModelUser
import ru.data.common.models.network.PasswordBody
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingUser
import ru.data.common.models.network.VerifyingEmailCode
import ru.data.common.models.network.VerifyingTelCode
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest
import java.io.File

class ApiUser(
    private val client: Client,
) {

    /**Получить Свой Профиль
     * BaseApi [NetworkModelUser]
     * */
    suspend fun getMe(
    ): BaseApi<NetworkModelUser> {
        return try {
            val response = client.api.get(urlString = "/test/users/me/")
            response.body<BaseApi<NetworkModelUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Изменить Текущего Пользователя
     * BaseApi [NetworkModelUser]
     * */
    suspend fun putMe(
        user: UpdatingUser
    ): BaseApi<NetworkModelUser> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/",
                body = user
            )
            response.body<BaseApi<NetworkModelUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Удалить Текущего Пользователя
     * BaseApi [String]
     * */
    suspend fun deleteMe(
    ): BaseApi<String> {
        return try {
            val response = client.api.delete(urlString = "/test/users/me/")
            response.body<BaseApi<String>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Изменить Аватар
     * BaseApi [NetworkModelUser]
     * */
    suspend fun postMyAvatar(
        file: File,
        type: MimeTypeAttachment = MimeTypeAttachment.IMAGE
    ): BaseApi<NetworkModelUser> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/avatar/",
                body = MultiPartFormDataContent(formData {
                    append("new_avatar", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, type.nameType)
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                })
            )
            response.body<BaseApi<NetworkModelUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /** Изменить Пароль Текущего Пользователя
     *
     * @param [Uri] PUT /test/users/me/password/
     *
     * @param [PasswordBody] body (body)
     *
     * @return [NetworkModelUser]
     */
    suspend fun putPassword(
        password: String
    ): BaseApi<NetworkModelUser> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/password/",
                body = PasswordBody(
                    password = password
                )
            )
            response.body<BaseApi<NetworkModelUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Изменить Email
     * BaseApi [String]
     * */
    suspend fun postMyEmail(
        email: String,
        code: String,
    ): BaseApi<String> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/email/",
                body = VerifyingEmailCode(
                    email = email,
                    code = code
                )
            )
            response.body<BaseApi<String>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Изменить Номер Телефона
     * BaseApi [String]
     * */
    suspend fun postMyTel(
        tel: String,
        code: String,
    ): BaseApi<String> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/tel/",
                body = VerifyingTelCode(
                    tel = tel,
                    code = code
                )
            )
            response.body<BaseApi<String>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}