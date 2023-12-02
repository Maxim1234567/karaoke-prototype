package ru.araok.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.araok.databinding.RecyclerInfoBinding

class RecyclerInfo
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {
    val recycler
        get() = binding.recycler

    val binding = RecyclerInfoBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    fun setShortDescription(shortDescription: String) {
        binding.shortDescription.text = shortDescription
    }

    fun <VH: RecyclerView.ViewHolder> setRecyclerAdapter(adapter: RecyclerView.Adapter<VH>) {
        binding.recycler.adapter = adapter
    }

    fun visibilityStateLoad(visible: Boolean) {
        if(visible) {
            binding.loadState.root.visibility = View.VISIBLE
        } else {
            binding.loadState.root.visibility = View.GONE
        }
    }
}