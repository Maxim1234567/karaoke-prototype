package ru.araok.custom

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import ru.araok.databinding.SearchLayoutBinding

class Search @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding = SearchLayoutBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setOnEditorActionListener(textChangedListener: (String) -> Unit) {
        binding.search.setOnEditorActionListener { textView, actionId, event ->
            if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                textChangedListener(textView.text.toString())
            }

            false
        }
    }
}