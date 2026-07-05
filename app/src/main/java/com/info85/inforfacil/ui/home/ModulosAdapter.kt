package com.info85.inforfacil.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.info85.inforfacil.databinding.ItemModuloBinding
import com.info85.inforfacil.models.ModuleItem
import com.info85.inforfacil.utils.toStarsString

class ModulosAdapter(
    private val onModuloClick: (ModuleItem) -> Unit
) : ListAdapter<ModuleItem, ModulosAdapter.ModuloViewHolder>(ModuloDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuloViewHolder {
        val binding = ItemModuloBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModuloViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModuloViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ModuloViewHolder(
        private val binding: ItemModuloBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(modulo: ModuleItem) {
            binding.apply {
                tvNomeModulo.text = modulo.nome
                tvDescricaoModulo.text = modulo.descricao
                tvNivel.text = "Nível ${modulo.nivel}"
                tvEstrelas.text = modulo.estrelas.toStarsString(modulo.totalEstrelas)
                progressBarModulo.progress = (modulo.percentualConcluido * 100).toInt()

                if (modulo.concluido) {
                    ivStatusConcluido.setImageResource(android.R.drawable.checkbox_on_background)
                    ivStatusConcluido.contentDescription = "Módulo concluído"
                } else {
                    ivStatusConcluido.setImageResource(android.R.drawable.checkbox_off_background)
                    ivStatusConcluido.contentDescription = "Módulo não concluído"
                }

                root.contentDescription = "${modulo.nome}. Nível ${modulo.nivel}. ${modulo.estrelas} estrelas. ${if (modulo.concluido) "Concluído" else "Em progresso"}"
                root.setOnClickListener { onModuloClick(modulo) }
            }
        }
    }

    private class ModuloDiffCallback : DiffUtil.ItemCallback<ModuleItem>() {
        override fun areItemsTheSame(oldItem: ModuleItem, newItem: ModuleItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ModuleItem, newItem: ModuleItem) =
            oldItem == newItem
    }
}
