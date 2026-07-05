package com.info85.inforfacil.ui.glossary

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.info85.inforfacil.content.GlossaryContent
import com.info85.inforfacil.content.GlossaryTerm
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivityGlossaryBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GlossaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlossaryBinding
    private lateinit var adapter: GlossaryAdapter
    private lateinit var repository: ProgressRepository

    private val terms = GlossaryContent.terms()
    private var favorites = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlossaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Glossário"

        repository = ProgressRepository(ProgressDataStore(applicationContext))

        setupRecycler()
        setupSearch()
        loadFavorites()
        filterList("")
    }

    private fun setupRecycler() {
        adapter = GlossaryAdapter(
            onToggleFavorite = { term -> toggleFavorite(term) },
            onDetails = { term -> showDetails(term) }
        )

        binding.rvGlossary.layoutManager = LinearLayoutManager(this)
        binding.rvGlossary.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            filterList(text?.toString().orEmpty())
        }
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            val progress = repository.progressComModulosDefault.first()
            favorites = progress.glossarioFavoritos.toMutableSet()
            adapter.setFavorites(favorites)
        }
    }

    private fun filterList(query: String) {
        val normalized = query.trim().lowercase()
        val filtered = if (normalized.isBlank()) {
            terms
        } else {
            terms.filter {
                it.term.lowercase().contains(normalized) ||
                    it.definition.lowercase().contains(normalized)
            }
        }
        binding.tvGlossaryCount.text = "${filtered.size} termos"
        adapter.submitList(filtered)
    }

    private fun toggleFavorite(term: GlossaryTerm) {
        lifecycleScope.launch {
            if (favorites.contains(term.id)) favorites.remove(term.id) else favorites.add(term.id)
            repository.atualizarFavoritosGlossario(favorites)
            adapter.setFavorites(favorites)
        }
    }

    private fun showDetails(term: GlossaryTerm) {
        AlertDialog.Builder(this)
            .setTitle(term.term)
            .setMessage(term.details)
            .setPositiveButton("Fechar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
