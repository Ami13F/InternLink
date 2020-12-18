package com.kotlinapp.model

import android.util.Log
import com.kotlinapp.auth.AuthApi
import com.kotlinapp.auth.AuthApi.authService
import com.kotlinapp.core.InternApi
import com.kotlinapp.core.persistence.ItemDao
import com.kotlinapp.utils.Result
import com.kotlinapp.utils.TAG

class CompanyRepository(itemDao: ItemDao) {

    var internships: List<InternshipDTO> = emptyList()
    var applications: List<ApplicationDTO> = emptyList()

    var users = itemDao.getAllUsers()

    suspend fun saveInternship(internship: Internship) = InternApi.saveInternship(internship)

    suspend fun getInternships(): Result<List<InternshipDTO>> {
        return try {
            val items = InternApi.service.getInternships()
            Log.d(TAG, "Internships from server: ${items.size}")

            internships = emptyList()
            for (item in items) {
                val company = InternApi.service.getCompany(item.companyId)
                internships = internships.plus(InternshipDTO(item.id!!, company.name, item.title))
            }
            Result.Success(internships)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getJobApplications(): Result<List<ApplicationDTO>> {
        return try {
            val jobApps = InternApi.service.getJobApplications()
            Log.d(TAG, "Applications from server: ${jobApps.size}")

            applications = emptyList()
            for (item in jobApps) {
                val internship = InternApi.service.getInternship(item.internshipId)
                val student = InternApi.service.getStudent(item.studentId)
                applications = applications.plus(
                    ApplicationDTO(
                        internship.title, student.firstName, student.lastName, item.status,
                        student.description, item.studentId, item.internshipId
                    )
                )
            }
            Result.Success(applications)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun changePassword(oldPass: String, newPasss: String): Result<Boolean> {
        return try {
            val resp = authService.changePass(AuthApi.PasswordChanger(oldPass, newPasss))
            Log.d(TAG, "Success resp... ${resp.code()}")
            if (resp.code() >= 400) {
                return Result.Error(null)
            }
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateStudent(student: Student): Result<Student> {
        return try {
            val studentUpdated = AuthApi.updateStudent(student)
            Result.Success(studentUpdated)
        }catch(e: Exception){
            Result.Error(e)
        }
    }
}