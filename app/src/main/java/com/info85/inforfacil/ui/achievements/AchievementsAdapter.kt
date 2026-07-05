package com.info85.inforfacil.ui.achievements

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.info85.inforfacil.content.AchievementDefinition
import com.info85.inforfacil.databinding.ItemAchievementBinding

data class AchievementUiItem(
    val definition: AchievementDefinition,
    val unlocked: Boolean,
    val unlockedDate: String?
)

class AchievementsAdapter : ListAdapter<AchievementUiItem, AchievementsAdapter.AchievementViewHolder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AchievementViewHolder(
        private val binding: ItemAchievementBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AchievementUiItem) {
            binding.ivAchievementIcon.setImageResource(item.definition.iconResId)
            binding.tvAchievementName.text = item.definition.name
            binding.tvAchievementDescription.text = item.definition.description
            if (item.unlocked) {
                binding.tvAchievementStatus.text = "Desbloqueada em ${item.unlockedDate ?: "hoje"}"
                binding.tvAchievementStatus.setTextColor(Color.parseColor("#2E7D32"))
                binding.ivAchievementIcon.alpha = 1f
            } else {
                binding.tvAchievementStatus.text = "Bloqueado"
                binding.tvAchievementStatus.setTextColor(Color.parseColor("#757575"))
                binding.ivAchievementIcon.alpha = 0.3f
            }
        }
    }

    private class Diff : DiffUtil.ItemCallback<AchievementUiItem>() {
        override fun areItemsTheSame(oldItem: AchievementUiItem, newItem: AchievementUiItem): Boolean =
            oldItem.definition.id == newItem.definition.id

        override fun areContentsTheSame(oldItem: AchievementUiItem, newItem: AchievementUiItem): Boolean =
            oldItem == newItem
    }
}
