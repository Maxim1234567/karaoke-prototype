package ru.araok.presentation.videopage

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.custom.VideoPlayer
import ru.araok.data.db.MarkDb
import ru.araok.data.db.SettingsDb
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
import ru.araok.databinding.FragmentVideoPageBinding
import ru.araok.presentation.ViewModelFactory
import ru.araok.presentation.markpage.PATH_VIDEO
import java.io.File
import javax.inject.Inject
import kotlin.streams.toList

const val CONTENT_ID = "contentId"

const val REQUEST_CODE_PERMISSION_VIDEO = 1001;

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class VideoPageFragment: Fragment() {
    private var _binding: FragmentVideoPageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: VideoPageViewModel by activityViewModels { viewModelFactory }

    private var mShouldScroll = false
    private var mToPosition = 0

    private var touchRecyclerView = false

    private val adapter = SubtitleAdapter()

    private val contentId: Long by lazy {
        arguments?.getLong(CONTENT_ID) ?: 0
    }

    private lateinit var videoPlayer: VideoPlayer

    override fun onStop() {
        super.onStop()
        binding.videoPlayer.stopSettings()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideoPageBinding.inflate(inflater, container, false)
        videoPlayer =  _binding?.videoPlayer!!
        viewModel.loadVideo(requireContext(), contentId)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.subtitle.adapter = adapter

        binding.subtitle.setOnTouchListener { _, _ ->
            touchRecyclerView = true
            false
        }

        binding.subtitle.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (mShouldScroll) {
                    mShouldScroll = false
                    smoothMoveToPosition(recyclerView, mToPosition)
                }

                if(newState == RecyclerView.SCROLL_STATE_IDLE && touchRecyclerView) {
                    val linearLayout = binding.subtitle.layoutManager as LinearLayoutManager
                    binding.videoPlayer.seekToSubtitleByIndex(linearLayout.findLastVisibleItemPosition())
                    touchRecyclerView = false
                }
            }
        })

        viewModel.video.onEach {
            if(it.isNotEmpty()) {
                val outputDir = requireActivity().cacheDir
                val file = File.createTempFile("temp", ".mp4", outputDir)
                file.writeBytes(it)

                binding.videoPlayer.setVideoPath(Uri.fromFile(file))
                binding.videoPlayer.play()
                binding.progressBar.visibility = View.GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.subtitle.onEach {
            adapter.setData(it)
            binding.videoPlayer.setSubtitleAndStart(it, ::updateSubtitleUI)

            if(it.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.settingsDb.onEach {
            if((it?.settingDb?.id != -1 && it?.settingDb?.contentId != -1) && it != null) {
                binding.videoPlayer.setSettings(
                    SettingsDto(
                        marks = it.marksDb.stream().map {
                            MarkDto(
                                id = it.id,
                                start = it.start,
                                end = it.end,
                                repeat = it.repeat,
                                delay = it.delay
                            )
                        }.toList()
                    )
                )
                binding.videoPlayer.startSettings()
                binding.progressBar.visibility = View.GONE
            }

            if (it == null) {
                viewModel.loadSettings(requireContext(), contentId)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.languageFlow.onEach {
            if(it != -1) {
                viewModel.sendLanguageId(-1)
                viewModel.loadSubtitle(requireContext(), contentId, it.toLong())
                binding.progressBar.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.settings.onEach {
            if(it.id != -1L && it.marks.isNotEmpty()) {
                binding.videoPlayer.setSettings(it)
                binding.videoPlayer.startSettings()

                val settingsWithMarksDb = SettingsWithMarksDb(
                    settingDb = SettingsDb(
                        contentId = contentId.toInt()),
                    marksDb = it.marks.stream().map {
                        MarkDb(
                            start = it.start,
                            end = it.end,
                            repeat = it.repeat,
                            delay = it.delay
                        )
                    }.toList()
                )

                viewModel.addSettingsWithMarks(settingsWithMarksDb)
                binding.progressBar.visibility = View.GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.start.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.loadSettingsDb(contentId)
        }

        binding.mark.setOnClickListener {
            binding.videoPlayer.stop()

            val bundle = bundleOf(
                CONTENT_ID to contentId,
                PATH_VIDEO to binding.videoPlayer.pathVideo.path
            )
            findNavController().navigate(R.id.video_page_to_mark_page, bundle)
        }

        binding.subtitleDownload.setOnClickListener {
            val bundle = bundleOf(
                CONTENT_ID to contentId
            )
            findNavController().navigate(R.id.video_page_to_subtitle_dialog, bundle)
        }
    }

    private fun updateSubtitleUI(index: Int) {
        if(!touchRecyclerView) {
            smoothMoveToPosition(binding.subtitle, index)
        }
    }

    private fun smoothMoveToPosition(recyclerView: RecyclerView, position: Int) {
        val firstItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
        // Последняя видимая позиция
        val lastItem =
            recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.childCount - 1));

        if (position < firstItem) {
            // Если позиция перехода находится перед первой видимой позицией, smoothScrollToPosition может перейти непосредственно
            recyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // Положение перехода - после первого видимого элемента и перед последним видимым элементом
            // smoothScrollToPosition не будет двигаться вообще, в это время smoothScrollBy вызывается для перемещения в указанную позицию
            val movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < recyclerView.childCount) {
                val top = recyclerView.getChildAt(movePosition).top;
                recyclerView.smoothScrollBy(0, top);
            }
        } else {
            // Если позиция для перехода находится после последнего видимого элемента, сначала вызовите smoothScrollToPosition, чтобы прокрутить позицию до видимой позиции
            // Вызовите smoothMoveToPosition снова через элемент управления onScrollStateChanged, чтобы выполнить метод в предыдущем решении
            recyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_PERMISSION_VIDEO -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
                    viewModel.loadVideo(requireContext(), contentId)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding.videoPlayer.stopSettings()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        videoPlayer.stop()
    }

    companion object {
        fun newInstance() = VideoPageFragment()
    }
}