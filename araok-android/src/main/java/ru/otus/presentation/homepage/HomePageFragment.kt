package ru.araok.presentation.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.consts.TypeContent
import ru.araok.custom.RecyclerInfo
import ru.araok.databinding.FragmentHomePageBinding
import ru.araok.presentation.ViewModelFactory
import ru.araok.presentation.videopage.CONTENT_ID
import javax.inject.Inject

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: HomePageViewModel by viewModels { viewModelFactory }

    private val adapterAll = HomePageAdapter(::onClick)
    private val adapterNew = HomePageAdapter(::onClick)
    private val adapterPopular = HomePageAdapter(::onClick)
    private val adapterRecommended = HomePageAdapter(::onClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        viewModel.loadAll(TypeContent.ALL)
        viewModel.loadNew(TypeContent.NEW)
        viewModel.loadPopular(TypeContent.POPULAR)
        viewModel.loadRecommended(TypeContent.RECOMMENDED)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSelectionLayout(
            binding.allLayout,
            R.string.all,
            adapterAll
        )

        setSelectionLayout(
            binding.newLayout,
            R.string._new,
            adapterNew
        )

        setSelectionLayout(
            binding.popularLayout,
            R.string.popular,
            adapterPopular
        )

        setSelectionLayout(
            binding.recommendedLayout,
            R.string.recommended,
            adapterRecommended
        )

        viewModel.all.onEach {
            adapterAll.setData(it)
            binding.allLayout.visibilityStateLoad(false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.new.onEach {
            adapterNew.setData(it)
            binding.newLayout.visibilityStateLoad(false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.popular.onEach {
            adapterPopular.setData(it)
            binding.popularLayout.visibilityStateLoad(false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.recommended.onEach {
            adapterRecommended.setData(it)
            binding.recommendedLayout.visibilityStateLoad(false)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setSelectionLayout(
        recyclerInfo: RecyclerInfo,
        shortDescription: Int,
        adapter: HomePageAdapter
    ) {
        recyclerInfo.setRecyclerAdapter(adapter)
        recyclerInfo.setShortDescription(resources.getString(shortDescription))
    }

    private fun onClick(id: Long) {
        val bundle = bundleOf(CONTENT_ID to id)
        findNavController().navigate(R.id.homepage_to_video_page, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }
}