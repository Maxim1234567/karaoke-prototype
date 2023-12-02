package ru.araok.presentation.registration

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.araok.data.dto.UserDto
import ru.araok.data.dto.UserWithJwtResponseDto
import ru.araok.domain.GetAraokUseCase
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class RegistrationViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _registration: Channel<UserWithJwtResponseDto> = Channel()
    val registration: Flow<UserWithJwtResponseDto> = _registration.receiveAsFlow()

    fun registerClient(user: UserDto) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.registration(user)
            }.fold(
                onSuccess = { _registration.send(it) },
                onFailure = { Log.d("RegistrationViewModel", "Error registration: ${it.message ?: ""}") }
            )
        }
    }
}