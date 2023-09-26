package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp

/**
 * Статусы дружбы
 *
 * 0 - не является другом и заявка не отправлена IS_NOT_A_FRIEND  -> кнопка отправить запрос
 *
 * 1 - вы отправили запрос, ожидается ответ YOU_SENT_RESPONSE_EXPECTED -> запрос отправлен (кнопка не активна)
 *
 * 2 - вам отправили запрос, ожидается ответ SENT_TO_YOU_RESPONSE_EXPECTED  -> принять в друзья
 *
 * 3 - вы отправили запрос, запрос принят YOU_SENT_REQUEST_ACCEPTED  -> кнопка написать
 *
 * 4 - вам отправили запрос, запрос принят SENT_TO_YOU_REQUEST_ACCEPTED -> кнопка написать
 *
 * 5 - вы отправили запрос, отклонён YOU_REJECTED  -> кнопка отправить запрос
 *
 * 6 - вам отправили запрос, отклонён YOUR_APPLICATION_WAS_REJECTED -> кнопка отправить запрос
 * */
enum class FriendshipStatuses {
    IS_NOT_A_FRIEND,
    YOU_SENT_RESPONSE_EXPECTED,
    SENT_TO_YOU_RESPONSE_EXPECTED,
    YOU_SENT_REQUEST_ACCEPTED,
    SENT_TO_YOU_REQUEST_ACCEPTED,
    YOU_REJECTED,
    YOUR_APPLICATION_WAS_REJECTED;

    companion object {
        fun getStatus(status: Int? = null) = entries.getOrElse(status ?: 0) { IS_NOT_A_FRIEND }
    }

    fun isEnabledButton(): Boolean = when (this) {
        YOU_SENT_RESPONSE_EXPECTED -> false
        else                       -> true
    }

    fun textButton(): String = when (this) {
        IS_NOT_A_FRIEND               -> TextApp.textBeFriendsWithCollectives
        YOU_SENT_RESPONSE_EXPECTED    -> TextApp.textRequestHasBeenSent
        SENT_TO_YOU_RESPONSE_EXPECTED -> TextApp.textAcceptAsFriend
        YOU_SENT_REQUEST_ACCEPTED     -> TextApp.textWrite
        SENT_TO_YOU_REQUEST_ACCEPTED  -> TextApp.textWrite
        YOU_REJECTED                  -> TextApp.textBeFriendsWithCollectives
        YOUR_APPLICATION_WAS_REJECTED -> TextApp.textBeFriendsWithCollectives
    }
}


