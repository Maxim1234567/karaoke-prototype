package ru.araok.presentation.search

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.araok.data.BASE_URL
import ru.araok.databinding.SearchItemBinding
import ru.araok.entites.Content

class SearchAdapter(
    private val onClick: (Long) -> Unit
): RecyclerView.Adapter<SearchViewHolder>() {
    private var data: List<Content> = emptyList()

    fun setData(data: List<Content>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder(
            SearchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        var item = data[position]

        with(holder.binding) {
            name.text = item.name
            artist.text = item.artist

            Glide.with(poster.context)
                .load(Uri.parse(BASE_URL + "/api/media/" + item.id + "/3"))
                .into(poster)

            holder.binding.root.setOnClickListener {
                onClick(item.id ?: 0)
            }
        }
    }
}

class SearchViewHolder(val binding: SearchItemBinding): RecyclerView.ViewHolder(binding.root)