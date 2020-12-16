package com.kotlinapp.internships

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kotlinapp.R
import com.kotlinapp.auth.data.UserRole
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.internships_fragment.*


class InternshipsFragment : Fragment() {

    private lateinit var itemListAdapter: InternshipDTOListAdapter
    private lateinit var itemsModel: InternshipViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.internships_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated Leaderboard...")
        if (AppPreferences.role == UserRole.STUDENT.toString())
            setupInternshipList()
        else if(AppPreferences.role == UserRole.COMPANY.toString())
            setupStudentsList()
    }

    private fun setupInternshipList() {
        itemListAdapter = InternshipDTOListAdapter(this)
        item_list.adapter = itemListAdapter

        itemsModel = ViewModelProvider(this).get(InternshipViewModel::class.java)
        itemsModel.getInternships()

        itemsModel.internships.observe(viewLifecycleOwner, Observer { items ->
            Log.v(TAG, "update items")
            itemListAdapter.internshipsDTO = items
        })

        itemsModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            Log.v(TAG, "update loading")
            progress.visibility = if (loading) View.VISIBLE else View.GONE
        })

        itemsModel.loadingError.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                Log.v(TAG, "update loading error")
                val message = "Loading exception ${exception.message}"
                val parentActivity = activity?.parent

                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupStudentsList(){
     //TODO: add students adapter
    }
}