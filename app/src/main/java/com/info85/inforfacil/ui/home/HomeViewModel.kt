package com.info85.inforfacil.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.info85.inforfacil.data.local.ProgressDataStore
import com.info85.inforfacil.data.local.ProgressModel
import com.info85.inforfacil.data.repository.ProgressRepository
import com.info85.inforfacil.models.ModuleItem
import com.info85.inforfacil.utils.Constants
import kotlinx.coroutines.flow.map

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ProgressDataStore(application)
    private val repository = ProgressRepository(dataStore)

    val progress: LiveData<ProgressModel> = repository.progressComModulosDefault.asLiveData()

    val modulosItems: LiveData<List<ModuleItem>> = repository.progressComModulosDefault.map { progress ->
        val modulosBase = Constants.getModulosDefault()
        modulosBase.map { modulo ->
            val moduloProgress = progress.modulos[modulo.id]
            modulo.copy(
                nivel = moduloProgress?.nivel ?: 1,
                estrelas = moduloProgress?.estrelas ?: 0,
                concluido = moduloProgress?.concluido ?: false,
                percentualConcluido = moduloProgress?.percentualConcluido ?: 0f
            )
        }
    }.asLiveData()
}
