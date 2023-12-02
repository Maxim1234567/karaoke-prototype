package ru.araok.presentation.addvideopage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.araok.R
import ru.araok.data.dto.AgeLimitDto
import ru.araok.databinding.FragmentAddVideoPageBinding
import ru.araok.presentation.ViewModelFactory
import javax.inject.Inject

const val REQUEST_CODE_PERMISSION = 1000;
const val RESULT_CODE_MEDIA = 2001;
const val RESULT_CODE_PREVIEW = 2002;
const val RESULT_CODE_SUBTITLE_ORIGINAL = 2003;
const val RESULT_CODE_SUBTITLE_TRANSLATE = 2004;

const val VIDEO_PAGE_FRAGMENT_TAG = "VIDEO_PAGE_FRAGMENT"

@AndroidEntryPoint
class VideoPageFragment: Fragment() {
    private var _binding: FragmentAddVideoPageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: AddVideoPageViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadAgeLimit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddVideoPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMedia.setOnClickListener {
            askPermissionAndBrowseFile(RESULT_CODE_MEDIA)
        }

        binding.btnPreview.setOnClickListener {
            askPermissionAndBrowseFile(RESULT_CODE_PREVIEW)
        }

        binding.btnSubtitleOriginal.setOnClickListener {
            askPermissionAndBrowseFile(RESULT_CODE_SUBTITLE_ORIGINAL)
        }

        binding.btnSubtitleTranslate.setOnClickListener {
            askPermissionAndBrowseFile(RESULT_CODE_SUBTITLE_TRANSLATE)
        }

        binding.btnAdd.setOnClickListener {
            binding.btnAdd.isEnabled = false
            viewModel.artist = binding.etArtist.text.toString()
            viewModel.songName = binding.etSongName.text.toString()
            viewModel.uploadMedia(
                requireContext(),
                requireActivity().contentResolver
            )
        }

        viewModel.ageLimits.onEach {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it.stream().map { a -> a.description }.toArray()
            )

            binding.ageLimitSpinner.adapter = adapter
            binding.ageLimitSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val id = it[position].id
                    val limit = it[position].limit
                    val description = it[position].description

                    viewModel.ageLimit = AgeLimitDto(id = id, limit = limit, description = description)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.content.onEach {
            binding.btnAdd.isEnabled = true
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun askPermissionAndBrowseFile(
        resultCodeFileChooser: Int
    ) {
        val permission = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION
            )

            return
        }

        doBrowseFile(resultCodeFileChooser)
    }

    private fun doBrowseFile(resultCodeFileChooser: Int) {
        var chooseFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        chooseFileIntent.type = "*/*"

        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE)

        chooseFileIntent = Intent.createChooser(chooseFileIntent, getString(R.string.choose_file))
        startActivityForResult(chooseFileIntent, resultCodeFileChooser)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            REQUEST_CODE_PERMISSION -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(requireContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RESULT_CODE_MEDIA -> {
                    data?.let {
                        binding.btnMedia.text = getPath(it)
                        viewModel.pathMedia = it.data
                    }
                }
                RESULT_CODE_PREVIEW -> {
                    data?.let {
                        binding.btnPreview.text = getPath(it)
                        viewModel.pathPreview = it.data
                    }
                }
                RESULT_CODE_SUBTITLE_ORIGINAL -> {
                    data?.let {
                        binding.btnSubtitleOriginal.text = getPath(it)
                        viewModel.pathSubtitleOriginal = getPath(it)
                    }
                }
                RESULT_CODE_SUBTITLE_TRANSLATE -> {
                    data?.let {
                        binding.btnSubtitleTranslate.text = getPath(it)
                        viewModel.pathSubtitleTranslate = getPath(it)
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getPath(data: Intent): String {
        val fileUri = data.data
        val inputStream = requireActivity().contentResolver.openInputStream(fileUri!!)
        return FileUtils.getPath(requireContext(), fileUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = VideoPageFragment()
    }
}