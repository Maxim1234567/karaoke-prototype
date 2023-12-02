package ru.araok.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.araok.presentation.homepage.HomePageViewModel
import ru.araok.presentation.language.LanguageViewModel
import ru.araok.presentation.search.SearchViewModel
import ru.araok.presentation.addvideopage.AddVideoPageViewModel
import ru.araok.presentation.authorization.AuthorizationViewModel
import ru.araok.presentation.markpage.MarkPageViewModel
import ru.araok.presentation.profile.ProfileViewModel
import ru.araok.presentation.registration.RegistrationViewModel
import ru.araok.presentation.videopage.VideoPageViewModel
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class ViewModelFactory @Inject constructor(
    private val addVideoPageViewModel: AddVideoPageViewModel,
    private val homePageViewModel: HomePageViewModel,
    private val searchViewModel: SearchViewModel,
    private val languageViewModel: LanguageViewModel,
    private val videoPageViewModel: VideoPageViewModel,
    private val markPageViewModel: MarkPageViewModel,
    private val profileViewModel: ProfileViewModel,
    private val registrationViewModel: RegistrationViewModel,
    private val authorizationViewModel: AuthorizationViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddVideoPageViewModel::class.java)) {
            return addVideoPageViewModel as T
        } else if(modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
            return homePageViewModel as T
        } else if(modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return searchViewModel as T
        } else if(modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            return languageViewModel as T
        } else if(modelClass.isAssignableFrom(VideoPageViewModel::class.java)) {
            return videoPageViewModel as T
        } else if(modelClass.isAssignableFrom(MarkPageViewModel::class.java)) {
            return markPageViewModel as T
        } else if(modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return profileViewModel as T
        } else if(modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return registrationViewModel as T
        } else if(modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            return authorizationViewModel as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}