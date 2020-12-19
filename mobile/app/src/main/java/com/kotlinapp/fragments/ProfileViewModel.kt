package com.kotlinapp.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kotlinapp.utils.TAG
import com.kotlinapp.model.CompanyRepository
import kotlinx.coroutines.launch
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.InternDatabase
import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
import com.kotlinapp.model.Student

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    private val mutableStudentUpdate = MutableLiveData<Student>()
    val studentUpdate = mutableStudentUpdate

    private val mutableCompanyUpdate = MutableLiveData<Company>()
    val companyUpdate = mutableCompanyUpdate

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    private val itemRepository: CompanyRepository

    init {
        val itemDao = InternDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = CompanyRepository(itemDao)
    }

    fun saveInternship(internship: Internship) {
        viewModelScope.launch {
            itemRepository.saveInternship(internship)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            Log.v(TAG, "Update Student Profile...")
            mutableFetching.value = true
            mutableException.value = null

            when (val result = itemRepository.updateStudent(student)) {
                is Result.Success -> {
                    Log.d(TAG, "Update succeeded")
                    mutableStudentUpdate.value = student
                }
                is Result.Error -> {
                    Log.w(TAG, "Update failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }

    fun updateCompany(company: Company) {
        viewModelScope.launch {
            Log.v(TAG, "Update Company Profile...")
            mutableFetching.value = true
            mutableException.value = null

            when (val result = itemRepository.updateCompany(company)) {
                is Result.Success -> {
                    Log.d(TAG, "Update succeeded")
                    mutableCompanyUpdate.value = company
                }
                is Result.Error -> {
                    Log.w(TAG, "Update failed", result.exception)
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }
}
