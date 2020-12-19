package com.kotlinapp.internships

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kotlinapp.core.InternApi
import com.kotlinapp.utils.TAG
import kotlinx.coroutines.launch
import java.lang.Exception
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.ItemDao
import com.kotlinapp.core.persistence.InternDatabase
import com.kotlinapp.model.*

class InternshipViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }
    private var mutableInternshipDTO =
        MutableLiveData<List<InternshipDTO>>().apply { value = emptyList() }

    private val mutableApplications =
        MutableLiveData<List<ApplicationDTO>>().apply { value = emptyList() }

    private val mutableInternship = MutableLiveData<Internship>().apply { value = null }

    val internship: LiveData<Internship> = mutableInternship
    val internships: LiveData<List<InternshipDTO>> = mutableInternshipDTO
    val applications: LiveData<List<ApplicationDTO>> = mutableApplications

    private val itemDao: ItemDao = InternDatabase.getDatabase(application, viewModelScope).itemDao()

    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val itemRepository: CompanyRepository

    init {
        itemRepository = CompanyRepository(itemDao)
    }

    fun getInternships() {
        viewModelScope.launch {
            Log.v(TAG, "Sorting by country...")
            mutableLoading.value = true
            mutableException.value = null
            when (val result = itemRepository.getInternships()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded")
                    mutableInternshipDTO.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }

    fun getJobApplications() {
        viewModelScope.launch {
            Log.v(TAG, "Sorting by country...")
            mutableLoading.value = true
            mutableException.value = null
            when (val result = itemRepository.getJobApplications()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded")
                    mutableApplications.value = result.data
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }

    fun getInternship(internshipDTO: InternshipDTO) {
        viewModelScope.launch {
            mutableInternship.value = InternApi.service.getInternship(internshipDTO.id)
        }
    }

    fun saveJobApplication(application: JobApplication) {
        viewModelScope.launch {
            InternApi.saveJobApplication(application)
        }
    }

    fun updateJobApplication(application: JobApplication) {
        viewModelScope.launch {
            InternApi.updateJobApplication(application)
            getJobApplications()
        }
    }
}
