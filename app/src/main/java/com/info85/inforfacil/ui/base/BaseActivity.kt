package com.info85.inforfacil.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.info85.inforfacil.utils.AppPreferences
import com.info85.inforfacil.utils.ThemeHelper

/**
 * Activity base que aplica automaticamente o tema, fontScale e modo escuro
 * definidos nas preferências do usuário.
 */
abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var appPreferences: AppPreferences

    override fun attachBaseContext(newBase: Context) {
        val prefs = AppPreferences(newBase)
        super.attachBaseContext(ThemeHelper.createFontScaledContext(newBase, prefs))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appPreferences = AppPreferences(this)
        ThemeHelper.applyTheme(this, appPreferences)
        super.onCreate(savedInstanceState)
    }
}
