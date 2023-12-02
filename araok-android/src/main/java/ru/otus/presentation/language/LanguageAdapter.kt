package ru.araok.presentation.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.araok.databinding.LanguageItemBinding
import ru.araok.entites.Language

class LanguageAdapter(
    private val onClick: (Int, String) -> Unit
): RecyclerView.Adapter<LanguageViewHolder>() {
    private var data: List<Language> = emptyList()

    fun setData(data: List<Language>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LanguageViewHolder(
            LanguageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        var item = data[position]

        with(holder.binding) {
            language.text = item.language

            holder.binding.root.setOnClickListener {
                onClick(item.id ?: 0, item.language)
            }
        }
    }
}

class LanguageViewHolder(val binding: LanguageItemBinding): RecyclerView.ViewHolder(binding.root)