package ru.araok.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class LanguageModelFactory @Inject constructor(
    private val languageViewModel: LanguageViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LanguageViewModel::class.java)) {
            return languageViewModel as T
        }

        throw IllegalArgumentException("Unknown class name")
    }
}