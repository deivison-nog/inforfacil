package com.info85.inforfacil.ui.module

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.info85.inforfacil.content.TopicItem
import com.info85.inforfacil.databinding.ItemTopicPageBinding
import com.info85.inforfacil.utils.ResourceResolver

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
            val resolvedImage = ResourceResolver.resolveDrawableByName(
                context = binding.root.context,
                resourceName = topic.imageResName,
                fallbackResId = topic.iconResId
            )
            val fallbackPadding = (12 * binding.root.resources.displayMetrics.density).toInt()

            binding.ivTopicIcon.setImageResource(resolvedImage.resId)
            binding.ivTopicIcon.scaleType = if (resolvedImage.resolvedByName) {
                ImageView.ScaleType.CENTER_CROP
            } else {
                ImageView.ScaleType.FIT_CENTER
            }
            if (resolvedImage.resolvedByName) {
                binding.ivTopicIcon.setPadding(0, 0, 0, 0)
            } else {
                binding.ivTopicIcon.setPadding(
                    fallbackPadding,
                    fallbackPadding,
                    fallbackPadding,
                    fallbackPadding
                )
            }
            binding.tvTopicTitle.text = topic.title
            binding.tvTopicDescription.text = topic.description
            binding.ivTopicIcon.contentDescription = "Imagem de ${topic.title}"
            binding.root.contentDescription = "${topic.title}. ${topic.description}"
        }
    }
}
