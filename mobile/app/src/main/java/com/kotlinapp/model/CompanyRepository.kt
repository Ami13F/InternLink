package com.kotlinapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.kotlinapp.auth.AuthApi
import com.kotlinapp.auth.AuthApi.authService
import com.kotlinapp.auth.data.User
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.core.InternApi
import com.kotlinapp.utils.Result
import com.kotlinapp.core.persistence.ItemDao
import com.kotlinapp.utils.TAG

class CompanyRepository (private val itemDao: ItemDao){

    var internships : List<InternshipDTO> = emptyList()
    var users = itemDao.getAllUsers()

    suspend fun saveInternship(internship: Internship) = InternApi.saveInternship(internship)

    suspend fun getInternships(): Result<List<InternshipDTO>> {
        return try {
            Log.d(TAG,"Refreshing...")
            val items = InternApi.service.getInternships()
            Log.d(TAG,"Internships from server: ${items.size}")


            for (item in items) {
                val company = InternApi.service.getCompany(item.companyId)
                internships = internships.plus(InternshipDTO(company.name, item.title))
            }
            Result.Success(internships)
        } catch(e: Exception) {
            Result.Error(e)
        }
    }

//    suspend fun updateUser(item: User): Result<User> {
//        return try {
//            val updatedItem = authService.updateUser(item.id!!)
//            Result.Success(updatedItem)
//        }catch(e: Exception){
//            Result.Error(e)
//        }
//    }

    suspend fun changePassword(oldPass: String, newPasss: String): Result<Boolean> {
        return try {
            val resp = authService.changePass(AuthApi.PasswordChanger(oldPass, newPasss))
            Log.d(TAG, "Success resp... ${resp.code()}")
            if (resp.code() >= 400) {
                return Result.Error(null)
            }
            Result.Success(true)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

//    suspend fun updateStudent(item: Student): Result<Student> {
//        return try {
//            val updatedItem = InternApi.service.update(item)
//            Result.Success(updatedItem)
//        }catch(e: Exception){
//            Result.Error(e)
//        }finally {
//            itemDao.update(item)
//        }
//    }
}