package ru.data.common.models.res

object TextApp {
    fun initDebugAndVersion(isDebug:Boolean, versionName:String){
        this.isDebug = isDebug
        this.versionName = versionName
    }
    var isDebug:Boolean= true
    var versionName:String= ""

    val mockEmail: String = if (isDebug) "123" else ""
    val mockPass: String = if (isDebug) "123" else ""
    

    const val baseTextNameApp: String = "Droid Tech"
    const val FOLDER_NAME: String = "Droid_Tech"
    const val linkTech: String = "https://tech.ru/"
    const val divForeRub: Int = 100
    const val TEXT_OS: String = "android"

    const val titleCancel: String = "Отмена"
    const val titleOk: String = "Ок"
    const val titleNext: String = "Продолжить"
    const val titleAdd: String = "Добавить"
    const val titleFillInLater: String = "Заполнить позже"
    const val titleExit: String = "Выход"
    const val titlePostType: String = "Тип постов"
    const val titleChange: String = "Изменить"
    const val titleAboutTheDeveloper: String = "О разработчике"
    const val titleRibbon: String = "Лента"
    const val titleAffairs: String = "Дела"
    const val titleAllPosts: String = "Все посты"
    const val titleStories: String = "Истории"
    const val titleSkip: String = "Пропустить"
    const val titleChats: String = "Чаты"
    const val textFrom: String = "от"
    const val textBefore: String = "до"
    const val titleCalendar: String = "Календарь"
    const val titlePhoto: String = "Фото"
    const val titleVideo: String = "Видео"
    const val titlePolls: String = "Опросы"
    const val titleGifts: String = "Подарки"
    const val titleChooseDroid: String = "Выбрать семью"
    const val titleEditProfile: String = "Редактировать профиль"
    const val titlePersonalData: String = "Личные данные"
    const val titleMedia: String = "Медиа"
    const val titleNewAlbum: String = "Новый альбом"
    const val titleWishList: String = "Желание"
    const val titleAwards: String = "Награды"
    const val titleCreate: String = "Создать"

    const val textSubject: String = "Тематика"
    const val textVisibleToAll: String = "Видно всем"
    const val textDroidOnly: String = "Только коллективе"
    const val textNow: String = "Сейчас"
    const val textNoSubject: String = "Без темы"

    const val titleAllRibbon: String = "Вся лента"
    const val titleDroidRibbon: String = "Семья"
    const val titleChat: String = "Чат"

    const val textPostType: String = "Тип постов"
    const val textAllCollectives: String = "Все семьи"

    const val textIncoming: String = "Входящие"
    const val textOutgoing: String = "Исходящие"

    const val textAddress: String = "Адрес"
    const val textMailNotRegistered: String = "Данная электронная почта не зарегистрирована в системе, для входа пройдите регистрацию"

    const val titleMyMedia: String = "Мои медиа"
    const val titleAll: String = "Все"
    const val titleDroid: String = "Семья"
    const val titleApplications: String = "Заявки"
    const val titleInvitations: String = "Приглашения"
    const val titleSearch: String = "Поиск"
    const val titleFavorites: String = "Избранное"

    const val holderTextPlus: String = "+"
    const val holderTextMinus: String = "-"
    const val holderDollar: String = "$"
    const val holderTextPhoneMask: String = "70000000000"
    const val holderName: String = "Имя"
    const val holderSurname: String = "Фамилия"
    const val holderPhone: String = "Телефон"
    const val holderTime: String = "Время"
    const val holderDate: String = "Дата"
    const val holderDroid: String = "Семья"
    const val holderContacts: String = "Контакты"
    const val holderPatronymic: String = "Отчество"
    const val holderSave: String = "Сохранить"
    const val holderFloor: String = "Этаж"
    const val holderImportant: String = "  ⃰"
    const val holderAsterisk: String = "⁂"
    const val holderSymbolStartDescription: String = "*-"
    const val holderMore: String = "Подробнее"
    const val holderShowAll: String = "Показать всех"
    const val holderShowAllS: String = "Показать все"
    const val holderEditProfile: String = "Редактировать профиль"
    const val holderViewAll: String = "Посмотреть всё"

    const val textExitApp: String = "Выйти из приложения?"
    const val textApplicationDevelopedDigital: String = "Приложение разработано\nв Digital студии"
    const val textMissingPermission: String = "Отсутствуют необходимые разрешения"
    val textVersionApp: String = "Версия $versionName"
    const val textStageConfirmed: String = "В работе"
    const val textOn: String = "включены"
    const val textInHourFeed: String = "В своей ленте"
    const val textInTheMessage: String = "В сообщении"
    const val textAddALink: String = "Добавьте ссылку"
    const val textLink: String = "Cсылкa"
    const val textDidNotChooseADroid: String = "Вы не выбрали семью или что-то пошло не так!"
    const val textTelegramNotInstalled: String = "Telegram не установлен!"
    const val textWhatsappNotInstalled: String = "Whatsapp не установлен!"
    const val textItemsOnSale: String = "Товары со скидкой"
    const val textFeaturedProducts: String = "Избранные товары"
    const val textSomethingWentWrong: String = "Что-то пошло не так"
    const val textTotal: String = "Итого"
    const val textErrorPagingContent: String = "Ошибка загрузки контента"

    const val textWishlistSecret: String = "Сделать вишлист секретным"
    const val textDescriptionWishlistSecret: String = "Видеть его сможете только вы и соавторы."
    const val textItEmpty: String = "Тут пусто"
    const val textComment: String = "Комментарий"
    const val textAnswerComment: String = "Ответ на комментарий"
    const val textMyDroid: String = "Моя семья"
    const val textRoleInTheDroid: String = "Роль в коллективе"
    const val textAcceptApplication: String = "Принять заявку"
    const val textAddToDroid: String = "Добавить в семью"
    const val textWriteAnAccompanying: String = "Пожалуйста, напишите сопроводительное письмо, чтобы пользователь положительно ответил на ваш запрос."
    const val textReject: String = "Отклонить"
    const val textCoveringLetter: String = "Сопроводительное письмо"
    const val textSort: String = "Сортировать"
    const val textItNoPhotosYet: String = "В этом вишлисте \nпока нет желаний"
    const val textCreateADroidCell: String = "Создать семейную ячейку"
    const val textJoinADroidUnit: String = "Присоединиться к семейной ячейке"
    const val textEnterTheDetailsOfYourDroidMembers: String =
        "Введите данные членов вашей семьи и отправьте им приглашение.*"
    const val textSignInWithAnInvite: String =
        "Войдите с помощью приглашения или ID семейной ячейки."
    const val textWelcome: String = "Добро пожаловать!"
    const val textCreateAnAccount: String = "Создание учётной записи"
    const val textEnterYourEmail: String = "Введите почту"
    const val textConfirmationCode: String = "Код подтверждения"
    const val textEnterConfirmationCode: String = "Введите код подтверждения"
    const val textTellUsAboutYourInterests: String = "Расскажите о ваших интересах"
    const val textCreateACell: String = "Создание ячейки"
    const val textEnterDroidCellID: String = "Введите ID семейной ячейки"
    const val textDroidCellID: String = "ID семейной ячейки"
    const val textWelcomeDroidTech: String = "Добро пожаловать\nв Droid Tech!"
    const val textEnterYourDetails: String = "Введите ваши данные."
    const val textEnterYourEmailDescription: String = "Введите электронную почту, которую указывали при регистрации"
    const val textYourDroidUnit: String = "Ваша семейная ячейка"
    const val textWelcomeInDroidTech: String = "Добро пожаловать в Droid Tech!"
    const val textRegisterWithEmail: String = "Зарегистрируйтесь с помощью электронной почты."
    const val textEmailAddress: String = "Адрес электронной почты"
    const val textPassword: String = "Пароль"
    const val textPasswordNoSimilar: String = "Пароли не совпадают"
    const val textPasswordNoRegular: String = "Пароль должен содержать минимум:" +
            "\n8 символов, одну латинскую букву, одну цифру"
    const val textEmailNoValide: String = "Неверный формат электронной почты"
    const val textPasswordTwo: String = "Повторите пароль"
    const val textPhoto: String = "Фотография"
    const val textName: String = "Имя*"
    const val textNameTitle: String = "Название"
    const val textComplain: String = "Пожаловаться"
    const val textBlockUser: String = "Заблокировать пользователя"
    const val textUserExist : String = "Такого пользователя еще не существует"
    const val textStateReasonComplain: String = "Укажите причину жалобы"
    const val textComplaint: String = "Жалоба"
    const val textInvite: String = "Запрос на дружбу"
    const val textChooseARoleInTheDroid: String = "Выберите роль в коллективе"
    const val textDeletingPost: String = "Удаление поста"
    const val textReallyWantToRemoveDesire: String = "Вы действительно хотите удалить желание?"
    const val textDeleteWishes: String = "Удаление желания."
    const val textDeleteWishesList: String = "Удаление листа желаний."
    const val textReallyWantToRemoveWishesList: String = "Вы действительно хотите удалить лист желаний?"
    const val textDescription: String = "Описание"
    const val textWish: String = "Желание"
    const val textWishUpdate: String = "Желание обновлено"
    const val textListWish: String = "Лист Желаний"
    const val textBook: String = "Забронировать"
    const val textBooked: String = "Забронировано"
    const val textGave: String = "Подарили"
    const val textAddSomething: String = "Добавить что-нибудь"
    const val textAddressOrPlace: String = "Адрес или место"
    const val buttonViewNextText: String = "развернуть"
    const val buttonGoneText: String = "скрыть"
    const val textNotUnreadNotifications: String = "У вас нет\nнепрочитанных уведомлений"


    const val textCreatingADroidDescription: String =
        "при создании семейной ячейки вводятся данные родителей и детей. Только родители (муж и жена) могут создать семейную ячейку."
    const val textBiography: String = "Биография"
    const val textInterests: String = "Интересы"
    const val textBirthDayWye: String = "Дата рождения*"
    const val textCustomDay: String = "Дата*"
    const val textBirthDay: String = "Дата рождения"
    const val textGender: String = "Пол*"
    const val textGender_: String = "Пол"
    const val textSurname: String = "Фамилия*"
    const val textGift: String = "Подарки"
    const val textNewWishlist: String = "Новый вишлист"
    const val textAlbumCreated: String = "Альбом создан"
    const val textCityOfBirth: String = "Город рождения"
    const val textMobilePhone: String = "Мобильный телефон"
    const val textNotSpecified: String = "Не указан"
    const val textTelegram: String = "Телеграм"
    const val textAddInterests: String = "Добавьте интересы"
    const val textEnterARequest: String = "Введите запрос"
    const val textPatronymic: String = "Отчество"
    const val textCityOfResidence: String = "Город проживания"
    const val textSpecifyGender: String = "Укажите пол"
    const val textDetailedInformation: String = "Подробная информация"
    const val textSearchFilter: String = "Фильтр поиска"
    const val textBirthday: String = "День рождения"
    const val textCity: String = "Город"
    const val textOriginalCity: String = "Родной город"
    const val textPhone: String = "Мобильный телефон"
    const val textPhoneUpdate: String = "Номер телефона обновлен"
    const val textTg: String = "Телеграм"
    const val textAboutYourself: String = "О себе"
    const val textDoing: String = "Деятельность"
    const val textCreateAPost: String = "Создать пост"

    //    const val textInterests: String = "Интересы"
    const val textLikeMusics: String = "Любимая музыка"
    const val textLikeFilms: String = "Любимые фильмы"
    const val textLikeBooks: String = "Любимые книги"
    const val textLikeGames: String = "Любимые игры"
    const val textMaidenName: String = "Девичья фамилия"
    const val textObligatoryField: String = "Обязательное поле"
    const val textIAmSuchAndSuch: String = "Я такой-то такой-то "
    const val textOpenTheCamera: String = "Открыть камеру"
    const val textOpenGallery: String = "Открыть галерею"
    const val textMailConfirmationCode: String = "Код подтверждения почты"
    const val textPhoneConfirmationCode: String = "Введите последние 4-е цифры из номера входящего звонка"
    const val textRegistered: String = "Регистрация"
    const val textForgotPassword: String = "Забыли пароль?"
    const val textToComeIn: String = "Войти"
    const val textDataUpdated: String = "Данные обновлены."
    const val textPrivateAlbum: String = "Приватный альбом"
    const val textPrivacyPolicy: String = "Политикой конфиденциальности"
    const val textForAMorePreciseSearch: String =
        "*-обязательные поля для более точного поиска совпадений с родственниками"
    const val textChangeEmailAddress: String = "Изменить адрес почты"
    const val textLinkAgreement: String = "согласие"
    const val textAgreementWhenInterPhone: String =
        "Даю $textLinkAgreement на обработку персональных данных"
    const val textLinkConditionsWhenInterPhone: String = "политики конфиденциальности"
    const val textConditionsWhenInterPhone: String =
        "Принимаю условия $textLinkConditionsWhenInterPhone"
    const val textAgreementPersonal: String =
        "Даю $textLinkAgreement на обработку персональных данных"
    const val textAgreementNews: String = "Даю $textLinkAgreement на получение новостей"
    const val textGenderOther: String = "Другое"
    const val textAny: String = "Любой"
    const val textIPreferNotToAnswer: String = "Предпочитаю не отвечать"
    const val textGenderMan: String = "Мужской"
    const val textGenderWoman: String = "Женский"
    const val textGenderManShort: String = "Муж"
    const val textGenderWomanShort: String = "Жена"
    const val textChildren: String = "Дети"
    const val textIncorrectCode: String = "Неверный код"
    const val textPhotos: String = "Фотографии"
    const val textAlbum: String = "Альбом"
    const val textViewAll: String = "Посмотреть всё"
    const val textUploadAPhoto: String = "Загрузить фото"
    const val textGrandParent: String = "Родители"
    const val textBrotherSister: String = "Брат/Сестра"

    const val textAddSpouse: String = "Добавить супруга"
    const val textAddChild: String = "Добавить детей"
    const val textAddSelf: String = "Добавить себя"
    const val textAddParent: String = "Добавить родителей"
    const val textAddSibling: String = "Добавить брата или сестру"
    const val textWrongLoginOrPassword: String = "Неверный логин или пароль"
    const val textSpouse: String = "Супруг"
    const val textChild: String = "Дети"
    const val textSelf: String = "Я"
    const val textParent: String = "Родители"
    const val textSibling: String = "Брат/Сестра"

    const val textAllFiles: String = "Все файлы"
    const val textWishlist: String = "Желания"
    const val textAlbums: String = "Альбомы"
    const val textAllWishes: String = "Все желания"
    const val textChooseAlbum: String = "Выберите альбом"
    const val textAnswer: String = "Ответить"
    const val textPost: String = "Пост"
    const val textShowAll: String = "Посмотреть все ответы"

    const val textSpam = "Спам"
    const val textAge = "Возраст"
    const val textNudityOrSexual = "Изображения обнаженного тела или действий сексуального характера"
    const val textHostileSayings = "Враждебные высказывания или символы Враждебные высказывания или символы"
    const val textViolenceOrDangerous = "Насилие или опасные организации"
    const val textBullyingOrPersecution = "Травля или преследования"
    const val textSaleIllegal = "Продажа незаконных товаров или товаров, подлежащих правовому регулированию"
    const val textViolationIntellectual = "Нарушение прав на интеллектуальную собственность"
    const val textSuicide = "Самоубийство или нанесение себе увечий"
    const val textEatingDisorder = "Расстройство пищевого поведения"
    const val textFraudOrDeception = "Мошенничество или обман"
    const val textFakeInformation = "Ложная информация"

    const val textAddToFavorite: String = "Добавить в избранное"
    const val textRemake: String = "Редактировать"
    const val textToComplain: String = "Пожаловаться"
    const val textSaveToYourFiles: String = "Сохранить в свои файлы"
    const val textGoToAlbum: String = "Перейти к альбому"
    const val textTitleEditPhoto: String = "Редактировать фото"
    const val textTitleDeletePhoto: String = "Удалить фото?"
    const val textRename: String = "Переименовать"
    const val textShare: String = "Поделиться"
    const val textDone: String = "Выполнено"
    const val textDownload: String = "Скачать"
    const val textSend: String = "Отправить"
    const val textDelete: String = "Удалить"
    const val textWrite: String = "Написать"
    const val textRequestHasBeenSent: String = "Запрос отправлен"
    const val textAcceptAsFriend: String = "Принять в друзья"
    const val textBeFriendsWithCollectives: String = "Дружить семьями"
    const val textRenameAlbum: String = "Переименовать альбом"
    const val textOwner: String = "Владелец"
    const val textDate: String = "Дата"
    const val textChangePicAlbum: String = "Изменить обложку"
    const val textRub: String = "₽"
    const val textPriceRub: String = "Цена, ₽"
    const val textPrice: String = "Цена"
    const val textThisIsAClosedAccount: String = "Это закрытый аккаунт"
    const val textMakeFriendsWithDroid: String = "Подружитесь с семьёй или добавьте в свою семью, чтобы иметь доступ к информации пользователя."
    const val textAddPhoto: String = "Добавить фотографии"
    const val textAddOnePhoto: String = "Добавить фото"
    const val textNewDesire: String = "Новое желание"
    const val textNewPost: String = "Новый пост"
    const val textPostEditing: String = "Редактирование поста"
    const val textPostPost: String = "Запостить"
    const val textBlock: String = "Заблокировать"
    const val textUpdatePost: String = "Обновить"
    const val textAnythingNew: String = "Что у вас нового?"
    const val textChangeDesire: String = "Изменить желание"
    const val textMyAlbom: String = "Мой альбом"
    const val textNoPhotoInAlbum: String = "В этом альбоме\nпока нет фотографий"
    const val textGenderInterWoman: String = "Введите данные вашей жены."
    const val textGenderInterMan: String = "Введите данные вашего мужа."
    const val textGenderInterSatellite: String = "Введите данные вашего спутника."
    const val textEnterChildren: String = "Введите данные выших детей."
    const val textCheckYourDroidDetails: String = "Проверьте данные вашей семьи"

    const val errorSomethingWrong: String = "Что-то пошло не так, попробуйте повторить позже"
    const val errorPagingContent: String = "Ошибка загрузки контента"
    const val errorEnterADeliveryAddress: String = "Укажите адрес доставки"
    const val errorEnterADeliveryEntrance: String = "Укажите номер подъезда"
    const val errorEnterADeliveryIntercom: String = "Укажите номер домофона"
    const val errorEnterADeliveryFloor: String = "Укажите этаж"
    const val errorEnterADeliveryApartmentOffice: String = "Укажите Квартиру/Офис"
    const val errorEnterADeliveryPhoneNumber: String = "Укажите номер телефона"
    const val errorCreateAlbum: String = "Ошибка при создании альбома"
    const val errorInvalidCodeEntered: String = "Введен неверный код"

    val formatNotRegisteredYet: (Any?) -> String = { "Ещё не зарегистрированы?   $it" }
    val formatYouAgreeTo: (Any?) -> String = { "Продолжая, вы соглашаетесь с $it" }
    val formatAlreadyHaveAnAccount: (Any?) -> String = { "Уже есть учётная запись?   $it" }
    val formatConfirmationCodeSentYourEmail: (Any?) -> String =
        { "На Ваш адрес электронной почты $it выслан код подтверждения. Введите код или перейдитепо ссылке из письма." }
    val formatDollar: (Any?) -> String = { " $it $" }
    val formatDownloadAlbum: (baseUrl: String,Any?) -> String = { baseUrl, str2 -> "$baseUrl/test/albums/$str2/download/" }
    val formatStepFrom: (Any?, Any?) -> String = { str1, str2 -> "Шаг $str1 из $str2" }
    val formatRub: (Any?) -> String = { "$it ₽" }
    val formatQty: (Any?) -> String = { " $it шт." }
    val formatReallyWantDeletingPost: (Any?) -> String = { "Вы действительно хотите удалить\"${it}...\"?" }
    val formatDeletingUserInDroid: (Any?) -> String = { "Удалить ${it ?: ""} из семьи?" }
    val formatBlockUser: (Any?) -> String = { "Вы действительно хотите заблокировать ${it ?: ""}?" }
    val formatWeight: (Any?) -> String = { "$it г" }
    val formatShowNews: (Any?, Boolean) -> String = { str, bool ->
        if (!bool) {
            "Показывать новости $str"
        } else
        {
            "Не показывать новости $str"
        }
    }
    val formatOrderNumber: (Any?) -> String = { "Ордер №$it" }
    val formatTextPhoneSend: (Any?) -> String = { "Будет произведен звонок на номер\n$it" }
    val formatPostedCreated: (Any?) -> String = { "Размещен $it" }
    val formatHelloUser: (Any?) -> String = { "Привет, $it" }
    val formatProductsFromSupplier: (Any?) -> String = { "Продукты от ${it ?: "..."}" }
    val formatPushNotificationsOnOff: (Any?) -> String = { "Push-уведомления ${it ?: ""}" }
    val formatSentARequestToJoin: (Any?, Any?) -> String =
        { str1, str2 -> "Вы отправили запрос на присоединение к семейной ячейки ${str1 ?: ""} ${str2 ?: ""}. Ожидайте, пока глава ячейки не примет ваше приглашение. " }
    val formatDeadlineDay: (Any?) -> String = { "$it дней" }
    val formatDataCreated: (Any?) -> String = { "Дата оформления: ${it ?: ""}" }
    val formatDeliveryTime: (Any?) -> String = { "Доставка к: ${it ?: ""}" }
    val formatChooseARoleInTheDroid: (Any?) -> String = { "Выберите роль в коллективе ${it?.let{str -> "для $str"} ?: ""}" }

    val formatGetCodeAgain: (Any?) -> String = { "Отправить код повторно через $it" }
    val formatDelete: (Any?) -> String = {
        "Фотография “$it”" + "будет удалена без возможности восстановления."
    }
    val formatSomethingYou: (Any?) -> String = { "$it (Вы)" }
    val formatAuthorFaq: (Any?, Any?) -> String = { str1, str2 -> "Автор: $str1, $str2" }
    val formatNumbOrderAndStatus: (Any?, Any?) -> String =
        { str1, str2 -> "№ ${str1 ?: "-"} ${str2 ?: ""}" }
    val formatUpdateFaq: (Any?) -> String = { str1 -> "Обновлено: $str1" }
    val textSelectedN: (Any?) -> String = { str -> "Выбрано: $str" }

}