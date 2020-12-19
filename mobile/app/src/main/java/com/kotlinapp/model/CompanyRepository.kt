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

            val applications : MutableList<ApplicationDTO> = mutableListOf()
            applications.clear()
            for (item in jobApps) {
                val internship = InternApi.service.getInternship(item.internshipId)
                val student = InternApi.service.getStudent(item.studentId)
                applications.add(
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

    suspend fun updateStudent(student: Student): Result<Student> {
        return try {
            val studentUpdated = AuthApi.updateStudent(student)
            Result.Success(studentUpdated)
        }catch(e: Exception){
            Result.Error(e)
        }
    }

    suspend fun updateCompany(company: Company): Result<Company> {
        return try {
            val companyUpdated = AuthApi.updateCompany(company)
            Result.Success(companyUpdated)
        }catch(e: Exception){
            Result.Error(e)
        }
    }
}