package com.info85.inforfacil.ui.achievements

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.info85.inforfacil.content.AchievementsContent
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivityAchievementsBinding
import com.info85.inforfacil.ui.base.BaseActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AchievementsActivity : BaseActivity() {

    private lateinit var binding: ActivityAchievementsBinding
    private lateinit var repository: ProgressRepository
    private lateinit var adapter: AchievementsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Conquistas"

        repository = ProgressRepository(ProgressDataStore(applicationContext))

        adapter = AchievementsAdapter()
        binding.rvAchievements.layoutManager = GridLayoutManager(this, 2)
        binding.rvAchievements.adapter = adapter

        loadAchievements()
    }

    private fun loadAchievements() {
        lifecycleScope.launch {
            val progress = repository.progressComModulosDefault.first()
            val unlocked = progress.conquistasDesbloqueadas

            val items = AchievementsContent.definitions().map { definition ->
                AchievementUiItem(
                    definition = definition,
                    unlocked = unlocked.containsKey(definition.id),
                    unlockedDate = unlocked[definition.id]
                )
            }

            adapter.submitList(items)
            val count = items.count { it.unlocked }
            binding.tvAchievementsProgress.text = "$count de ${items.size} conquistas desbloqueadas"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
