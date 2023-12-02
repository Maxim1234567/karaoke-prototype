package ru.araok.presentation.profile

import android.os.Build
import android.util.JsonToken
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.data.dto.UserDto
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.User
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class ProfileViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _user = MutableStateFlow<User>(UserDto(id = -1))
    val user: StateFlow<User> = _user.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _user.value
    )

    fun user(accessToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getUser(accessToken)
            }.fold(
                onSuccess = { _user.value = it },
                onFailure = { Log.d("ProfileViewModel", it.message ?: "") }
            )
        }
    }
}