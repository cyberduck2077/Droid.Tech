package ru.data.common.network.api


import io.ktor.client.call.body
import ru.data.common.models.network.CreatingEmailVerificationCode
import ru.data.common.models.network.CreatingTelVerificationCode
import ru.data.common.models.network.NetworkModelTelVerificationCode
import ru.data.common.models.network.LoginData
import ru.data.common.models.network.BaseApi
import ru.data.common.models.network.SignUpEmail
import ru.data.common.models.network.NetworkModelTokenWithUser
import ru.data.common.models.network.VerificationCode
import ru.data.common.models.network.VerifyingEmailCode
import ru.data.common.models.network.formatInException
import ru.data.common.network.Client
import ru.data.common.network.util.postRequest

class ApiSignIn(
    private val client: Client,
) {

    /**Войти По Логину И Паролю
     * BaseApi [NetworkModelTokenWithUser]
     * */
    suspend fun postSignIn(
        email: String,
        password: String,
    ): BaseApi<NetworkModelTokenWithUser> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/sign-in/email/",
                body = LoginData(email = email, password = password)
            )
         response.body<BaseApi<NetworkModelTokenWithUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /**Оправить Код Подтверждения На Телефон
     * BaseApi [NetworkModelTelVerificationCode]
     * */
    suspend fun postVerificationTel(
        tel: String,
    ): BaseApi<NetworkModelTelVerificationCode> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/verification-codes/tel/",
                body = CreatingTelVerificationCode(tel = tel)
            )

        response.body<BaseApi<NetworkModelTelVerificationCode>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Оправить Код Подтверждения На Email
     * BaseApi [VerificationCode]
     * */
    suspend fun postEmailApi(
        email: String,
    ): BaseApi<VerificationCode> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/verification-codes/email/",
                body = CreatingEmailVerificationCode(email = email)
            )
            response.body<BaseApi<VerificationCode>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }

    /**Регистрация С Помощью Email
     * BaseApi [NetworkModelTokenWithUser]
     * */
    suspend fun postRegApi(
        email: String,
        password: String,
        code: String,
    ): BaseApi<NetworkModelTokenWithUser> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/sign-up/email/",
                body = SignUpEmail(
                    email = email,
                    password = password,
                    code = code
                )
            )
       response.body<BaseApi<NetworkModelTokenWithUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }


    /** Войти По Email И Коду Подтверждения
     *
     * @param [Uri] POST /test/sign-in/otp/
     *
     * @param [VerifyingEmailCode] body (body)
     *
     * @return [NetworkModelTokenWithUser]
     */
    suspend fun postSignInOtp(
        email: String,
        code: String,
    ): BaseApi<NetworkModelTokenWithUser> {
        return try {
            val response = client.api.postRequest(
                urlString = "/test/sign-in/otp/",
                body = VerifyingEmailCode(
                    email = email,
                    code = code
                )
            )
       response.body<BaseApi<NetworkModelTokenWithUser>>().apply {
                status = response.status
            }
        } catch (e: Exception) {
            formatInException(e)
        }
    }
}