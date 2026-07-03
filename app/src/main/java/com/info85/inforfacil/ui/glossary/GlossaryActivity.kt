package com.info85.inforfacil.ui.glossary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.info85.inforfacil.databinding.ActivityGlossaryBinding

class GlossaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGlossaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGlossaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Glossário"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
