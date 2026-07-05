package com.info85.inforfacil

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.info85.inforfacil.workers.DailyReminderWorker
import java.util.concurrent.TimeUnit

class InfoFacilApp : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleDailyReminder()
    }

    private fun scheduleDailyReminder() {
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WORK_REMINDER,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    companion object {
        private const val WORK_REMINDER = "inforfacil_daily_reminder"
    }
}
