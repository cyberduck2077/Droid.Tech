package ru.droid.tech.di

import org.koin.dsl.module
import ru.droid.tech.screens.forgot_password.ForgotPasswordModule
import ru.droid.tech.screens.module_authorization.AuthScreenModel
import ru.droid.tech.screens.module_main.chat_user.ChatUserModel
import ru.droid.tech.screens.module_main.create_new_album.CreateNewAlbumModel
import ru.droid.tech.screens.module_main.info_user.InfoUserModel
import ru.droid.tech.screens.module_main.main_gifts.GiftsModel
import ru.droid.tech.screens.module_main.main_ribbon.RibbonModel
import ru.droid.tech.screens.module_main.media.album.AlbumModel
import ru.droid.tech.screens.module_main.media_and_albums_all.MediaAndAlbumsAllModel
import ru.droid.tech.screens.module_main.media_list.MediaListModel
import ru.droid.tech.screens.module_main.media_edit.EditMediaModel
import ru.droid.tech.screens.module_main.notification.NotificationModel
import ru.droid.tech.screens.module_main.post_new.NewPostModel
import ru.droid.tech.screens.module_main.post_with_comment.PostWithCommentModel
import ru.droid.tech.screens.module_main.profile.ProfileModel
import ru.droid.tech.screens.module_main.profile_redaction.ProfileRedactionModel
import ru.droid.tech.screens.module_main.search_users.SearchUsersModule
import ru.droid.tech.screens.module_main.show_Droid.ShowDroidModel
import ru.droid.tech.screens.module_main.wish_detail.DetailWishModel
import ru.droid.tech.screens.module_main.wish_list.WishListModel
import ru.droid.tech.screens.module_main.wish_list_new.NewWishListModel
import ru.droid.tech.screens.module_main.wish_new.NewWishModel
import ru.droid.tech.screens.module_main.wish_update.UpdateWishModel
import ru.droid.tech.screens.module_registration.RegStepsModel
import ru.droid.tech.screens.module_registration.step_1.StepOneModel
import ru.droid.tech.screens.splash.SplashScreenModel


val setModels = module {
    single { RegStepsModel(get(), get(), get(), get(), get(), get()) }
    single { ProfileRedactionModel(get(), get(), get(), get(), get(), get()) }
    single { MediaAndAlbumsAllModel(get(), get(), get(), get()) }

    factory { AlbumModel(get(), get(), get()) }
    factory { StepOneModel(get()) }
    factory { AuthScreenModel(get()) }
    factory { SplashScreenModel(get(), get(), get()) }
    factory { RibbonModel(get(), get(), get(), get()) }
    factory { EditMediaModel(get()) }
    factory { NewPostModel(get(), get()) }
    factory { MediaListModel(get()) }
    factory { CreateNewAlbumModel(get()) }
    factory { GiftsModel(get()) }
    factory { NewWishModel(get(), get()) }
    factory { ChatUserModel() }
    factory { ForgotPasswordModule(get(), get(), get()) }
    factory { NotificationModel() }
    factory { SearchUsersModule(get(), get()) }
    factory { ShowDroidModel(get(), get(), get(), get(), get()) }
    factory { WishListModel(get()) }
    factory { DetailWishModel(get()) }
    factory { InfoUserModel(get(), get()) }
    factory { UpdateWishModel(get(), get()) }
    factory { NewWishListModel(get()) }
    factory { ProfileModel(get(), get(), get(), get(), get(), get()) }
    factory { PostWithCommentModel(get()) }
}