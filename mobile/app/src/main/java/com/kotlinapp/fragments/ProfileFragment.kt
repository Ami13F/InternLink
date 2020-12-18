package com.kotlinapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kotlinapp.R
import com.kotlinapp.auth.data.UserRole
import com.kotlinapp.core.AppPreferences

open class ProfileFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!AppPreferences.isLogin)
            findNavController().navigate(R.id.login_fragment)
        else if(AppPreferences.role == UserRole.COMPANY.toString())
            findNavController().navigate(R.id.company_profile_fragment)
        else if(AppPreferences.role == UserRole.STUDENT.toString())
            findNavController().navigate(R.id.student_profile_fragment)
    }

}