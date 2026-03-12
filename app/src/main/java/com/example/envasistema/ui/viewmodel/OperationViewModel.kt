package com.example.envasistema.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.envasistema.data.repository.OperationRepository
import com.example.envasistema.util.OperationPayload
import kotlinx.coroutines.launch

class OperationViewModel(private val repository: OperationRepository) : ViewModel() {

    /**
     * Saves the operation payload to the local Room database via the repository.
     * This also triggers the background sync WorkManager task.
     */
    fun saveOperation(payload: OperationPayload) {
        viewModelScope.launch {
            repository.saveOperation(payload)
        }
    }
}

class OperationViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OperationViewModel(OperationRepository(application)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
