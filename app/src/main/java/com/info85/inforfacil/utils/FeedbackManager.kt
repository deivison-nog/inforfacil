package com.info85.inforfacil.utils

import android.content.Context
import android.media.MediaPlayer
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
            playSound("feedback_success", RingtoneManager.TYPE_NOTIFICATION)
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 80, 40, 80), false)
        }
    }

    fun playError() {
        if (prefs.somAtivado) {
            playSound("feedback_error", RingtoneManager.TYPE_RINGTONE)
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 200, 100, 200), false)
        }
    }

    fun playModuleComplete() {
        if (prefs.somAtivado) {
            playSound("feedback_module_complete", RingtoneManager.TYPE_NOTIFICATION)
        }
        if (prefs.vibracaoAtivada) {
            vibrate(longArrayOf(0, 100, 50, 100, 50, 200), false)
        }
    }

    private fun playSound(resourceName: String, fallbackToneType: Int) {
        try {
            val rawResId = ResourceResolver.rawResIdByName(context, resourceName)
            if (rawResId != null) {
                MediaPlayer.create(context, rawResId)?.apply {
                    setOnCompletionListener { player -> player.release() }
                    setOnErrorListener { player, _, _ ->
                        player.release()
                        true
                    }
                    start()
                } ?: playDefaultTone(fallbackToneType)
            } else {
                playDefaultTone(fallbackToneType)
            }
        } catch (_: Exception) {
            playDefaultTone(fallbackToneType)
        }
    }

    private fun playDefaultTone(toneType: Int) {
        try {
            val uri = RingtoneManager.getDefaultUri(toneType)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        } catch (_: Exception) {}
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
