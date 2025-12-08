package com.maxi.dailyfeed.presentation.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.maxi.dailyfeed.databinding.ItemNewsBinding
import com.maxi.dailyfeed.domain.model.Article

class NewsAdapter : ListAdapter<Article,
        NewsAdapter.NewsViewHolder>(
    NewsDiffUtilCallBack()
) {

    class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(article: Article) {
            binding.apply {
                article.apply {
                    tvTitle.text = title
                    tvDescp.text = description
                    tvUrl.text = url

                    Glide
                        .with(binding.root.context)
                        .load(imageUrl)
                        .into(ivNews)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder =
        NewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: NewsViewHolder,
        position: Int
    ) {
        holder.onBind(getItem(position))
    }
}

class NewsDiffUtilCallBack : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(
        oldItem: Article,
        newItem: Article
    ): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(
        oldItem: Article,
        newItem: Article
    ): Boolean =
        oldItem == newItem

}