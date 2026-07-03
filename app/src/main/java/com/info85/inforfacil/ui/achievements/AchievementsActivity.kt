package com.info85.inforfacil.ui.achievements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.info85.inforfacil.databinding.ActivityAchievementsBinding

class AchievementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Conquistas"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
