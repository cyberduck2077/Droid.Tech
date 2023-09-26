package ru.data.common.network.api

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.apache.tika.Tika
import ru.data.common.models.network.CreatingMediaFromAttachment
import ru.data.common.models.network.NetworkModelAttachment
import ru.data.common.models.network.NetworkModelMedia
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.UpdatingAttachment
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest
import ru.data.common.network.util.putRequest
import java.io.File

/**Клиентское приложение / Вложения*/
class ApiAttachments(private val client: Client) {

    /**
     * Добавить Файл
     * @param [Uri] POST /test/users/me/attachments/
     * @param [File] file (body)
     * @return [NetworkModelAttachment]
     */
    suspend fun postAttachment(
        file: File,
    ): BaseApi<NetworkModelAttachment> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/me/attachments/",
                body = MultiPartFormDataContent(formData {
                    append("new_file", file.readBytes(), Headers.build {
                        append(HttpHeaders.ContentType, Tika().detect(file))
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    })
                })
            )
           response.body<BaseApi<NetworkModelAttachment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Изменить Вложения
     * @param [Uri] PUT /test/users/me/attachments/{attachment_id}/
     * @param [Int] attachment_id (path)
     * @param [UpdatingAttachment] (body)
     * @return [NetworkModelAttachment]
     */
    suspend fun putAttachment(
        attachmentId: Int,
        body: UpdatingAttachment,
    ): BaseApi<NetworkModelAttachment> {
        return try {
            val response = client.api.putRequest(
                urlString = "/test/users/me/attachments/${attachmentId}/",
                body = body
            )
            response.body<BaseApi<NetworkModelAttachment>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Удалить Вложения
     * @param [Uri] DELETE /test/users/me/attachments/{attachment_id}/
     * @param [Int] attachment_id (path)
     * @return [Nothing]
     */
    suspend fun deleteAttachment(
        attachmentId: Int,
    ): BaseApi<Any> {
        return try {
            val response = client.api.delete(
                urlString = "/test/users/me/attachments/${attachmentId}/",
            )
           response.body<BaseApi<Any>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

    /**
     * Добавить Вложение В Медиа
     * @param [Uri] POST /test/users/attachments/{attachment_id}/media/
     * @param [CreatingMediaFromAttachment] (body)
     * @return [NetworkModelMedia]
     */
    suspend fun postAddInMedia(
        attachmentId: Int,
        body: CreatingMediaFromAttachment,
    ): BaseApi<NetworkModelMedia> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/users/attachments/${attachmentId}/media/",
                body = body
            )
           response.body<BaseApi<NetworkModelMedia>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            e.printStackTrace()
            BaseApi.getError(description = e.message, errorCode = e.hashCode())
        }
    }

}