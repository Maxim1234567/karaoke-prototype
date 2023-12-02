package ru.araok.presentation.search

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
import ru.araok.entites.Content
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getAraokUseCase: GetAraokUseCase
): ViewModel() {

    private val _search = MutableStateFlow<List<Content>>(emptyList())
    val search: StateFlow<List<Content>> = _search.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = _search.value
    )

    fun search(search: String) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                getAraokUseCase.getContentsByName(search)
            }.fold(
                onSuccess = { _search.value = it },
                onFailure = { Log.d("SearchViewModel", it.message ?: "") }
            )
        }
    }
}