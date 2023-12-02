package ru.araok.presentation.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.araok.data.dto.JwtRequestDto
import ru.araok.data.dto.JwtResponseDto
import ru.araok.domain.GetAraokUseCase
import javax.inject.Inject

class AuthorizationViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _login: Channel<JwtResponseDto> = Channel()
    val login: Flow<JwtResponseDto> = _login.receiveAsFlow()

    fun loginClient(jwtRequestDto: JwtRequestDto) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.login(jwtRequestDto)
            }.fold(
                onSuccess = { _login.send(it) },
                onFailure = { Log.d("AuthorizationViewModel", "Error login: ${it.message ?: ""}") }
            )
        }
    }
}