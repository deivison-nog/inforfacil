package com.info85.inforfacil.ui.splash

import android.content.Intent
import android.os.Bundle
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.databinding.ActivitySplashBinding
import com.info85.inforfacil.ui.base.BaseActivity
import com.info85.inforfacil.ui.home.HomeActivity
import com.info85.inforfacil.ui.onboarding.OnboardingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            delay(1800)
            updateDailyStreak()
            withContext(Dispatchers.Main) {
                navigateNext()
            }
        }
    }

    private suspend fun updateDailyStreak() {
        try {
            val repository = ProgressRepository(ProgressDataStore(applicationContext))
            repository.registrarAcessoDiario()
        } catch (_: Exception) {}
    }

    private fun navigateNext() {
        val dest = if (appPreferences.isFirstLaunch) {
            Intent(this, OnboardingActivity::class.java)
        } else {
            Intent(this, HomeActivity::class.java)
        }
        startActivity(dest)
        finish()
    }
}

