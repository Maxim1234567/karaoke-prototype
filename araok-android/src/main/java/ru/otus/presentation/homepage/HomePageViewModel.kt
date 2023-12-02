package ru.araok.presentation.homepage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.araok.consts.TypeContent
import ru.araok.domain.GetAraokUseCase
import ru.araok.entites.Content
import javax.inject.Inject

class HomePageViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {
    private val _all = MutableStateFlow<List<Content>>(emptyList())
    val all: StateFlow<List<Content>> = _all.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _all.value
    )

    private val _new = MutableStateFlow<List<Content>>(emptyList())
    val new: StateFlow<List<Content>> = _new.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _new.value
    )

    private val _popular = MutableStateFlow<List<Content>>(emptyList())
    val popular: StateFlow<List<Content>> = _popular.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _popular.value
    )

    private val _recommended = MutableStateFlow<List<Content>>(emptyList())
    val recommended: StateFlow<List<Content>> = _recommended.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _recommended.value
    )

    fun loadAll(type: TypeContent) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getContents(type)
            }.fold(
                onSuccess = { _all.value = it },
                onFailure = { Log.d("HomePageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadNew(type: TypeContent) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getContents(type)
            }.fold(
                onSuccess = { _new.value = it },
                onFailure = { Log.d("HomePageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadPopular(type: TypeContent) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getContents(type)
            }.fold(
                onSuccess = { _popular.value = it },
                onFailure = { Log.d("HomePageViewModel", it.message ?: "") }
            )
        }
    }

    fun loadRecommended(type: TypeContent) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getContents(type)
            }.fold(
                onSuccess = { _recommended.value = it },
                onFailure = { Log.d("HomePageViewModel", it.message ?: "") }
            )
        }
    }
}