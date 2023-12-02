package ru.araok.presentation.language

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.araok.databinding.ActivityLanguageBinding
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity: AppCompatActivity() {
    private var _binding: ActivityLanguageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: LanguageModelFactory
    private val viewModel: LanguageViewModel by viewModels { viewModelFactory }

    private val adapter = LanguageAdapter(::onClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getAllLanguages()

        binding.recycler.adapter = adapter

        viewModel.language.onEach {
            adapter.setData(it)
        }.launchIn(lifecycleScope)
    }

    private fun onClick(id: Int, language: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = LanguageActivity()
    }
}