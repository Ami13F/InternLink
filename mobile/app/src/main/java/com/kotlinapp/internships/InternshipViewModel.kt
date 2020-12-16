package com.kotlinapp.internships

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.kotlinapp.auth.data.User
import com.kotlinapp.utils.TAG
import com.kotlinapp.model.CompanyRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.ItemDao
import com.kotlinapp.core.persistence.InternDatabase
import com.kotlinapp.model.InternshipDTO

class InternshipViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }
    private var mutableInternshipDTO = MutableLiveData<List<InternshipDTO>>().apply { value = emptyList() }

    val internships: LiveData<List<InternshipDTO>> = mutableInternshipDTO
    val users: LiveData<List<User>>

    private val itemDao: ItemDao = InternDatabase.getDatabase(application, viewModelScope).itemDao()
//    var leaderCountryList: LiveData<List<ItemDao.BoardItem>>

    //    var country = AppPreferences.getCurrentPlayer().country
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val itemRepository: CompanyRepository

    init {
        itemRepository = CompanyRepository(itemDao)
//        internships = itemDao.getAllInternships()
        users = itemRepository.users
//        leaderList = itemRepository.leaders
//        leaderCountryList = itemDao.getSortedByCountry(country)
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

}
