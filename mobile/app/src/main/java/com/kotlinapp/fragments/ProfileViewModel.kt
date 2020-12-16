package com.kotlinapp.fragments

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kotlinapp.utils.TAG
import com.kotlinapp.model.CompanyRepository
import kotlinx.coroutines.launch
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.InternDatabase
import com.kotlinapp.R
import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
import com.kotlinapp.model.Student

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }


    private val mutablePasswordState = MutableLiveData<PasswordState>()
    val passwordState: LiveData<PasswordState> = mutablePasswordState

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

    fun validatePasswords(oldPass: String, newPass1: String, newPass2: String) {
        when {
            oldPass.length < 6 -> {
                mutablePasswordState.value =
                    PasswordState(oldPasswordError = R.string.invalid_password)
            }
            newPass1.length < 6 -> {
                mutablePasswordState.value =
                    PasswordState(newPassword1Error = R.string.invalid_password)
            }
            newPass2.length < 6 -> {
                mutablePasswordState.value =
                    PasswordState(newPassword2Error = R.string.invalid_password)
            }
            newPass1 != newPass2 -> {
                mutablePasswordState.value =
                    PasswordState(newPassword2Error = R.string.same_passwords)
            }
            else -> {
                mutablePasswordState.value =
                    PasswordState(isValid = true)
            }
        }
    }

    fun updateProfile(student: Student) {
        //TODO: uncomment this
//        viewModelScope.launch {
//            Log.v(TAG, "Update Profile...")
//            mutableFetching.value = true
//            mutableException.value = null
//
//            when(val result= itemRepository.updateStudent(student)) {
//                is Result.Success -> {
//                    Log.d(TAG, "Update succeeded")
//                    mutablePlayerUpdate.value = student
//                }
//                is Result.Error -> {
//                    Log.w(TAG, "Update failed", result.exception)
//                    mutableException.value = result.exception
//                }
//            }
//            mutableFetching.value = false
//        }
    }

    fun changePassword(oldPass: String, newPass: String) {
        viewModelScope.launch {
            Log.d(TAG, "Change password....")
            mutableFetching.value = true
            mutableException.value = null

            when (val result = itemRepository.changePassword(oldPass, newPass)) {
                is Result.Success -> {
                    Log.d(TAG, "Update succeeded")
                    mutableCompleted.value = true
                }
                is Result.Error -> {
                    Log.w(TAG, "Update failed", result.exception)
                    mutablePasswordState.value =
                        PasswordState(oldPasswordError = R.string.invalid_old_password)
                    mutableException.value = result.exception
                }
            }
            mutableFetching.value = false
        }
    }
}
