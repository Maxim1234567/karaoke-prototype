package ru.araok.presentation.videopage

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.araok.databinding.SubtitleItemBinding
import ru.araok.entites.Subtitle

class SubtitleAdapter: RecyclerView.Adapter<SubtitleViewHolder>() {

    private var data: List<Subtitle> = emptyList()

    fun setData(data: List<Subtitle>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SubtitleViewHolder(
            SubtitleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        var item = data[position]

        with(holder.binding) {
            subtitle.text = item.line
        }
    }
}

class SubtitleViewHolder(val binding: SubtitleItemBinding): RecyclerView.ViewHolder(binding.root)