package ru.data.common.models.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import ru.data.common.models.res.TextApp

data class BaseApi<T>(
    val data: T? = null,
    val description: String? = null,
    val errors: List<ErrorApi>? = null,
    val message: String? = null,
    var status: HttpStatusCode = HttpStatusCode.OK,
    val meta: MetaApi? = null,
) {

    companion object {
        fun <T> getError(
            description: String? = null,
            errors: List<ErrorApi>? = null,
            errorCode: Int? = null
        ): BaseApi<T> {
            val str = TextApp.textSomethingWentWrong
            return BaseApi(
                data = null,
                description = description ?: str,
                errors = if (errors.isNullOrEmpty()) listOf(ErrorApi(
                    message = description ?: str,
                    code = errorCode ?: 508
                )) else errors,
                message = null,
                status = HttpStatusCode(errorCode ?: 508, description ?: str),
                meta = null
            )
        }
    }

    fun getDescriptionRudApi() = if (this.description.isNullOrBlank()) {
        TextApp.textSomethingWentWrong
    } else {
        this.description.take(150)
    }

    fun formattedErrors(): String {
        var errors = ""
        this.errors?.forEach { error ->
            errors += "${error.message.toString()}\n"
        }
        return errors.trim()
    }

    fun getCodeResponse(): Int {
        if (this.data != null) return 200
        val firstError = this.errors?.firstOrNull()
        return firstError?.code ?: 508
    }
}

data class ErrorApi(
    val additional: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val path: String? = null
)

data class MetaApi(
    val paginator: PaginationApi? = null
)

data class PaginationApi(
    val has_next: Boolean? = null,
    val has_prev: Boolean? = null,
    val page: Int? = null,
    val total: Int? = null
)

fun <RUD> formatInException(e: Exception): BaseApi<RUD> {
    println("formatInException\n" + e.message)
    e.printStackTrace()
    return when (e) {
        is NoTransformationFoundException -> BaseApi.getError(
            description = "NoTransformationFoundException",
            errorCode = e.hashCode()
        )

        is ConnectTimeoutException -> BaseApi.getError(
            description = "ConnectTimeoutException",
            errorCode = e.hashCode()
        )

        else                              -> BaseApi.getError(
            description = e.message,
            errorCode = e.hashCode()
        )

    }

}

suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }




