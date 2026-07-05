package com.info85.inforfacil.ui.glossary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.info85.inforfacil.content.GlossaryTerm
import com.info85.inforfacil.databinding.ItemGlossaryTermBinding

class GlossaryAdapter(
    private val onToggleFavorite: (GlossaryTerm) -> Unit,
    private val onDetails: (GlossaryTerm) -> Unit
) : ListAdapter<GlossaryTerm, GlossaryAdapter.GlossaryViewHolder>(Diff()) {

    private var favoriteIds: Set<String> = emptySet()

    fun setFavorites(favorites: Set<String>) {
        favoriteIds = favorites
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlossaryViewHolder {
        val binding = ItemGlossaryTermBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GlossaryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GlossaryViewHolder, position: Int) {
        holder.bind(getItem(position), favoriteIds.contains(getItem(position).id))
    }

    inner class GlossaryViewHolder(
        private val binding: ItemGlossaryTermBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GlossaryTerm, favorite: Boolean) {
            binding.ivGlossaryIcon.setImageResource(item.iconResId)
            binding.tvTerm.text = item.term
            binding.tvDefinition.text = item.definition
            binding.btnFavorite.text = if (favorite) "★" else "☆"
            binding.btnFavorite.contentDescription = if (favorite) {
                "Remover ${item.term} dos favoritos"
            } else {
                "Adicionar ${item.term} aos favoritos"
            }

            binding.btnFavorite.setOnClickListener { onToggleFavorite(item) }
            binding.root.setOnClickListener { onDetails(item) }
        }
    }

    private class Diff : DiffUtil.ItemCallback<GlossaryTerm>() {
        override fun areItemsTheSame(oldItem: GlossaryTerm, newItem: GlossaryTerm) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GlossaryTerm, newItem: GlossaryTerm) = oldItem == newItem
    }
}
