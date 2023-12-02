package ru.araok.presentation.homepage

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.araok.data.BASE_URL
import ru.araok.databinding.ContentItemBinding
import ru.araok.entites.Content

class HomePageAdapter(
    private val onClick: (Long) -> Unit
): RecyclerView.Adapter<HomePageViewHolder>() {
    private var data: List<Content> = emptyList()

    fun setData(data: List<Content>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HomePageViewHolder(
            ContentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HomePageViewHolder, position: Int) {
        var item = data[position]

        with(holder.binding) {
            name.text = item.name

            Glide.with(poster.context)
                .load(Uri.parse(BASE_URL + "/api/media/" + item.id + "/3"))
                .into(poster)

            holder.binding.root.setOnClickListener {
                onClick(item.id ?: 0)
            }
        }
    }
}

class HomePageViewHolder(val binding: ContentItemBinding): RecyclerView.ViewHolder(binding.root)