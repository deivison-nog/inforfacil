package com.info85.inforfacil.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "inforfacil_progress")

class ProgressDataStore(private val context: Context) {

    private val gson = Gson()

    private object Keys {
        val PROGRESS_JSON = stringPreferencesKey("progress_json")
    }

    val progressFlow: Flow<ProgressModel> = context.dataStore.data.map { preferences ->
        val json = preferences[Keys.PROGRESS_JSON]
        if (json.isNullOrEmpty()) {
            ProgressModel()
        } else {
            try {
                val type = object : TypeToken<ProgressModel>() {}.type
                gson.fromJson(json, type) ?: ProgressModel()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao desserializar progresso do DataStore", e)
                ProgressModel()
            }
        }
    }

    suspend fun saveProgress(progress: ProgressModel) {
        val json = gson.toJson(progress)
        context.dataStore.edit { preferences ->
            preferences[Keys.PROGRESS_JSON] = json
        }
    }

    suspend fun updateModuloProgress(moduloId: String, moduloProgress: ModuloProgress) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[Keys.PROGRESS_JSON]
            val currentProgress = if (currentJson.isNullOrEmpty()) {
                ProgressModel()
            } else {
                try {
                    val type = object : TypeToken<ProgressModel>() {}.type
                    gson.fromJson(currentJson, type) ?: ProgressModel()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao desserializar progresso ao atualizar módulo", e)
                    ProgressModel()
                }
            }
            val updatedModulos = currentProgress.modulos.toMutableMap()
            updatedModulos[moduloId] = moduloProgress
            val totalEstrelas = updatedModulos.values.sumOf { it.estrelas }
            val percentualGeral = if (updatedModulos.isNotEmpty()) {
                updatedModulos.values.map { it.percentualConcluido }.average().toFloat()
            } else 0f
            val updatedProgress = currentProgress.copy(
                modulos = updatedModulos,
                totalEstrelas = totalEstrelas,
                percentualGeral = percentualGeral
            )
            preferences[Keys.PROGRESS_JSON] = gson.toJson(updatedProgress)
        }
    }

    suspend fun updateConfiguracoes(configuracoes: Configuracoes) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[Keys.PROGRESS_JSON]
            val currentProgress = if (currentJson.isNullOrEmpty()) {
                ProgressModel()
            } else {
                try {
                    val type = object : TypeToken<ProgressModel>() {}.type
                    gson.fromJson(currentJson, type) ?: ProgressModel()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao desserializar progresso ao atualizar configurações", e)
                    ProgressModel()
                }
            }
            val updatedProgress = currentProgress.copy(configuracoes = configuracoes)
            preferences[Keys.PROGRESS_JSON] = gson.toJson(updatedProgress)
        }
    }

    suspend fun clearProgress() {
        context.dataStore.edit { preferences ->
            preferences.remove(Keys.PROGRESS_JSON)
        }
    }

    companion object {
        private const val TAG = "ProgressDataStore"
    }
}
