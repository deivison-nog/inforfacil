package com.info85.inforfacil.utils

import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Gerencia feedback de som e vibração para ações do usuário.
 */
class FeedbackManager(private val context: Context) {

    private val prefs = AppPreferences(context)

    @Suppress("DEPRECATION")
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    fun playSuccess() {
        if (prefs.somAtivado) {
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } catch (_: Exception) {}
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 80, 40, 80), false)
        }
    }

    fun playError() {
        if (prefs.somAtivado) {
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } catch (_: Exception) {}
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 200, 100, 200), false)
        }
    }

    fun playModuleComplete() {
        if (prefs.somAtivado) {
            try {
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } catch (_: Exception) {}
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 100, 50, 100, 50, 200), false)
        }
    }

    @Suppress("DEPRECATION")
    private fun vibrate(pattern: LongArray, repeat: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createWaveform(pattern, if (repeat) 0 else -1)
                vibrator.vibrate(effect)
            } else {
                vibrator.vibrate(pattern, if (repeat) 0 else -1)
            }
        } catch (_: Exception) {}
    }
}
