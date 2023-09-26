package ru.data.common.models.local.maps

import ru.data.common.models.res.TextApp


/**
 * Причина жалобы
 *
 * 0 - Спам
 *
 * 1 - Изображения обнаженного тела или действий сексуального характера
 *
 * 2 - Враждебные высказывания или символы Враждебные высказывания или символы
 *
 * 3 - Насилие или опасные организации
 *
 * 4 - Травля или преследования
 *
 * 5 - Продажа незаконных товаров или товаров, подлежащих правовому регулированию
 *
 * 6 - Нарушение прав на интеллектуальную собственность
 *
 * 7 - Самоубийство или нанесение себе увечий
 *
 * 8 - Расстройство пищевого поведения
 *
 * 9 - Мошенничество или обман
 *
 * 10 - Ложная информация
 *
 * */
enum class TypeReason {
    SPAM,
    NUDITY_OR_SEXUAL,
    HOSTILE_SAYINGS,
    VIOLENCE_OR_DANGEROUS,
    BULLYING_OR_PERSECUTION,
    SALE_ILLEGAL,
    VIOLATION_INTELLECTUAL,
    SUICIDE,
    EATING_DISORDER,
    FRAUD_OR_DECEPTION,
    FAKE_INFORMATION;


    fun getDescription() = when (this) {
        SPAM                    -> TextApp.textSpam
        NUDITY_OR_SEXUAL        -> TextApp.textNudityOrSexual
        HOSTILE_SAYINGS         -> TextApp.textHostileSayings
        VIOLENCE_OR_DANGEROUS   -> TextApp.textViolenceOrDangerous
        BULLYING_OR_PERSECUTION -> TextApp.textBullyingOrPersecution
        SALE_ILLEGAL            -> TextApp.textSaleIllegal
        VIOLATION_INTELLECTUAL  -> TextApp.textViolationIntellectual
        SUICIDE                 -> TextApp.textSuicide
        EATING_DISORDER         -> TextApp.textEatingDisorder
        FRAUD_OR_DECEPTION      -> TextApp.textFraudOrDeception
        FAKE_INFORMATION        -> TextApp.textFakeInformation
    }


}