package ru.araok.presentation.subtitle

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.databinding.FragmentDialogSubtitleBinding
import ru.araok.presentation.ViewModelFactory
import ru.araok.presentation.language.LanguageAdapter
import ru.araok.presentation.videopage.VideoPageViewModel
import javax.inject.Inject

private const val CONTENT_ID = "contentId"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class SubtitleDialogFragment: DialogFragment() {
    private var _binding: FragmentDialogSubtitleBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: VideoPageViewModel by activityViewModels { viewModelFactory }

    private val adapter = LanguageAdapter(::onClick)

    private val contentId: Long by lazy {
        arguments?.getLong(CONTENT_ID) ?: 0
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getAllLanguageSubtitle(requireContext(), contentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogSubtitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        viewModel.language.onEach {
            adapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(id: Int, language: String) {
        viewModel.sendLanguageId(id)
        dismiss()
    }
}