package com.info85.inforfacil.ui.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.info85.inforfacil.content.TopicItem
import com.info85.inforfacil.databinding.ItemTopicPageBinding

class TopicPagerAdapter(
    private val topics: List<TopicItem>
) : RecyclerView.Adapter<TopicPagerAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemTopicPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    override fun getItemCount(): Int = topics.size

    class TopicViewHolder(private val binding: ItemTopicPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: TopicItem) {
            binding.ivTopicIcon.setImageResource(topic.iconResId)
            binding.tvTopicTitle.text = topic.title
            binding.tvTopicDescription.text = topic.description
            binding.root.contentDescription = "${topic.title}. ${topic.description}"
        }
    }
}
