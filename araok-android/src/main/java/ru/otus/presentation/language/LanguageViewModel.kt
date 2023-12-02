package ru.araok.presentation.language

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.Language
import javax.inject.Inject

class LanguageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _language = MutableStateFlow<List<Language>>(emptyList())
    val language: StateFlow<List<Language>> = _language.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _language.value
    )

    fun getAllLanguages() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.gtAllLanguages()
            }.fold(
                onSuccess = { _language.value = it },
                onFailure = { Log.d("LanguageViewModel", it.message ?: "") }
            )
        }
    }
}