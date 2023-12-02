package ru.araok.presentation.markpage

import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.araok.R
import ru.araok.custom.MaskWatcher
import ru.araok.data.db.MarkDb
import ru.araok.data.db.SettingsDb
import ru.araok.data.db.SettingsWithMarksDb
import ru.araok.data.dto.MarkDto
import ru.araok.data.dto.SettingsDto
import ru.araok.databinding.FragmentMarkPageBinding
import ru.araok.milliSecondsToTimer
import ru.araok.presentation.ViewModelFactory
import ru.araok.timerToMilliSeconds
import java.io.File
import java.util.UUID
import javax.inject.Inject
import kotlin.streams.asStream
import kotlin.streams.toList

const val CONTENT_ID = "contentId"
const val PATH_VIDEO = "pathVideo"

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MarkPageFragment: Fragment() {
    private var _binding: FragmentMarkPageBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MarkPageViewModel by viewModels { viewModelFactory }

    private val contentId: Int by lazy {
        arguments?.getLong(CONTENT_ID)?.toInt() ?: 0
    }

    private val pathVideo: String by lazy {
        arguments?.getString(PATH_VIDEO) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadSettingsDb(contentId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarkPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load.onEach {
            if(it == State.LOAD) {
                binding.progressBar.visibility = View.GONE
                binding.save.isEnabled = true
            } else if(it == State.PROCESS) {
                binding.progressBar.visibility = View.VISIBLE
                binding.save.isEnabled = false
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.videoPlayer.setVideoPath(Uri.fromFile(File(pathVideo)))
        binding.videoPlayer.play()

        binding.newMark.setOnClickListener {
            createNewMark(
                start = binding.videoPlayer.currentPosition(),
                end = binding.videoPlayer.duration(),
                repeat = "1",
                delay = "1"
            )
        }

        binding.save.setOnClickListener {
            val settings = getSettings()
            val settingsWithMarksDb = SettingsWithMarksDb(
                settingDb = SettingsDb(
                    contentId = contentId
                ),
                marksDb = settings.marks.stream().map {
                    m -> MarkDb(
                        start = m.start,
                        end = m.end,
                        repeat = m.repeat,
                        delay = m.delay
                    )
                }.toList()
            )

            viewModel.addSettingsWithMarks(requireContext(), settingsWithMarksDb)
        }

        viewModel.settingsDb.onEach {
            if(it != null) {
                it.marksDb.forEach {
                    createNewMark(
                        start = it.start!!,
                        end = it.end!!,
                        repeat = it.repeat?.toString()!!,
                        delay = it.delay?.toString()!!
                    )
                }

                binding.progressBar.visibility = View.GONE
            } else {
                viewModel.loadSettings(requireContext(), contentId.toLong())
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.settings.onEach {
            if(it.id != -1L && it.marks.isNotEmpty()) {
                val settingsWithMarksDb = SettingsWithMarksDb(
                    settingDb = SettingsDb(contentId = contentId),
                    marksDb = it.marks.stream().map {
                        MarkDb(
                            start = it.start,
                            end = it.end,
                            repeat = it.repeat,
                            delay = it.delay
                        )
                    }.toList()
                )

                viewModel.addSettingsWithMarksOnlyDb(settingsWithMarksDb)

                settingsWithMarksDb.marksDb.forEach {
                    createNewMark(
                        start = it.start!!,
                        end = it.end!!,
                        repeat = it.repeat?.toString()!!,
                        delay = it.delay?.toString()!!
                    )
                }

                binding.progressBar.visibility = View.GONE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun createNewMark(
        start: Int,
        end: Int,
        repeat: String,
        delay: String
    ) {
        val textViewPrefixTo = createTextView(resources.getString(R.string.prefix_to))
        val textViewPrefixFrom = createTextView(resources.getString(R.string.prefix_from))
        val textViewPrefixRepeat = createTextView(resources.getString(R.string.prefix_repeat))
        val textViewPrefixDelay = createTextView(resources.getString(R.string.prefix_delay))
        val editTextTo = createEditTextRange(milliSecondsToTimer(start))
        val editTextFrom = createEditTextRange(milliSecondsToTimer(end))
        val editTextRepeat = createEditTextNumber(repeat)
        val editTextDelay = createEditTextNumber(delay)
        val delete = createImageView(R.drawable.mark_delete)
        val play = createImageView(R.drawable.play)
        val linearLayout = linearLayout()

        linearLayout.tag = UUID.randomUUID().toString()

        linearLayout.addView(textViewPrefixTo)
        linearLayout.addView(editTextTo)
        linearLayout.addView(textViewPrefixFrom)
        linearLayout.addView(editTextFrom)
        linearLayout.addView(textViewPrefixRepeat)
        linearLayout.addView(editTextRepeat)
        linearLayout.addView(textViewPrefixDelay)
        linearLayout.addView(editTextDelay)
        linearLayout.addView(delete)
        linearLayout.addView(play)

        binding.llNewMarks.addView(linearLayout)

        delete.tag = binding.llNewMarks.childCount
        delete.setOnClickListener {
            val view = binding.llNewMarks.children.asStream().filter { v -> v.tag == linearLayout.tag }.findFirst()

            if(view.isPresent) {
                binding.llNewMarks.removeView(view.get())
            }
        }

        play.setOnClickListener {
            val view = getLayoutByUuid(linearLayout.tag as String)
            val mark = layoutToMarkDto(view)
            binding.videoPlayer.playRange(mark)
        }
    }

    private fun linearLayout(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.HORIZONTAL

        return linearLayout
    }

    private fun createTextView(prefix: String): TextView {
        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        textView.textSize = 15.0f
        textView.setTextColor(resources.getColor(R.color.black))
        textView.text = prefix
        textView.typeface = Typeface.DEFAULT_BOLD

        return textView
    }
    private fun createEditTextRange(value: String): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        editText.setText(value)
        editText.filters = arrayOf(numberFilter(), InputFilter.LengthFilter(5))
        editText.addTextChangedListener(MaskWatcher("##:##"))

        return editText
    }

    private fun createEditTextNumber(value: String): EditText {
        val editText = EditText(context)
        editText.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        editText.setText(value)
        editText.filters = arrayOf(numberFilter())

        return editText
    }

    private fun createImageView(image: Int): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(image)
        imageView.layoutParams = LinearLayout.LayoutParams(
            100, 100, 1.0f
        )

        return imageView
    }

    private fun getSettings(): SettingsDto {
        val marks = ArrayList<MarkDto>()

        binding.llNewMarks.children.forEachIndexed { index, view ->
            marks.add(layoutToMarkDto(view))
        }

        return SettingsDto(marks = marks)
    }

    private fun getLayoutByUuid(uuid: String) =
        binding.llNewMarks.children.asStream()
            .filter { v -> v.tag == uuid }
            .findFirst()
            .orElse(LinearLayout(context))

    private fun layoutToMarkDto(view: View): MarkDto {
        val linearLayout = view as LinearLayout
        val editTextTo = linearLayout.getChildAt(1) as EditText
        val editTextFrom = linearLayout.getChildAt(3) as EditText
        val editTextRepeat = linearLayout.getChildAt(5) as EditText
        val editTextDelay = linearLayout.getChildAt(7) as EditText

        return MarkDto(
            id = id,
            start = timerToMilliSeconds(editTextTo.text.toString()),
            end = timerToMilliSeconds(editTextFrom.text.toString()),
            repeat = editTextRepeat.text.toString().toInt(),
            delay = editTextDelay.text.toString().toInt()
        )
    }

    private fun numberFilter(): InputFilter {
        return InputFilter { source, start, end, dest, dstart, dend ->
            if(source.isEmpty())
                return@InputFilter ""

            if(Character.isDigit(source[0]))
                return@InputFilter source

            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoPlayer.stop()
        _binding = null
    }

    companion object {
        fun newInstance() = MarkPageFragment()
    }
}