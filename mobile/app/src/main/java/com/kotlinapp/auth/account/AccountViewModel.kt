package com.kotlinapp.auth.account

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.kotlinapp.auth.AuthApi
import com.kotlinapp.auth.data.User
import com.kotlinapp.auth.login.ValidateFormState
import com.kotlinapp.utils.TAG
import com.kotlinapp.model.Company
import com.kotlinapp.model.CompanyRepository
import kotlinx.coroutines.launch
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.LitterDatabase
import com.kotlinapp.R
import com.kotlinapp.model.Student

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private var mutableExceptionEmail = MutableLiveData<Exception?>().apply { value = null }
    private val mutableExceptionUsername = MutableLiveData<Exception>().apply { value = null }

    private val exitsEmail = MutableLiveData<Boolean>().apply { value = false }
    val emailExists = exitsEmail
    private val usernameValid = MutableLiveData<Boolean>().apply { value = false }
    val userNameExists = usernameValid

    private val mutableValidForm = MutableLiveData<ValidateFormState>()
    val validFormState = mutableValidForm

    val completed: LiveData<Boolean> = mutableCompleted

    private val itemRepository: CompanyRepository

    init {
        val itemDao = LitterDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = CompanyRepository(itemDao)
    }

    fun validateCreateAccount(email: String, password: String, validateEmail: Boolean){
       Log.d(TAG, "Validating.....")
        if(validateEmail && !validateEmail(email)){
           mutableValidForm.value = ValidateFormState(emailError = R.string.invalid_email_format)
       }else if(validateEmail && exitsEmail.value!!){
           mutableValidForm.value = ValidateFormState(emailError = R.string.email_exists)
        }else if(password.isEmpty()){
           mutableValidForm.value = ValidateFormState(passwordError = R.string.invalid_password)
       }else if(password.contains("!@#$%^&*()")){
            mutableValidForm.value = ValidateFormState(passwordError = R.string.invalid_password_chars)
        } else {
           mutableValidForm.value = ValidateFormState(isDataValid = true)
       }
    }

    private fun validateEmail(email: String): Boolean{
        Log.d(TAG, "Validating email: $email")
        val pattern = Regex("[a-zA-Z0-9\\-]{1,35}@[a-zA-Z0-9\\-]{1,35}\\.[a-zA-Z0-9\\-]{1,10}")
        val match = pattern.containsMatchIn(email)
        if (!match)
            return false
        return true
    }

    fun saveAccount(user: User, company: Company){
        viewModelScope.launch {
            Log.v(TAG, "Create account...")
            when(val result =  AuthApi.createCompanyAccount(user, company)) {
                is Result.Success -> {
                    //Because we can't override default value from id in loopback
//                    itemRepository.updateUser(user)
                    mutableCompleted.value = true
                }
                is Result.Error ->{
                    Log.w(TAG, "Failed create company account ${result?.exception} \n Message: ${result?.exception!!.message}")
                }
            }
        }
    }

    fun saveStudentAccount(user: User, student: Student){
        viewModelScope.launch {
            Log.v(TAG, "Create account...")
            when(val result =  AuthApi.createStudentAccount(user, student)) {
                is Result.Success -> {
                    //Because we can't override default value from id in loopback
//                    itemRepository.updateUser(user)
                    mutableCompleted.value = true
                }
                is Result.Error ->{
                    Log.w(TAG, "Failed create company account ${result?.exception} \n Message: ${result?.exception!!.message}")
                }
            }
        }
    }
}
