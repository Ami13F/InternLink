package com.kotlinapp.internships

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kotlinapp.R
import com.kotlinapp.auth.data.UserRole
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.model.*
import com.kotlinapp.students.StudentsListAdapter
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.internships_fragment.*


class InternshipsFragment : Fragment() {

    private lateinit var itemListAdapter: InternshipDTOListAdapter
    private lateinit var studentListAdapter: StudentsListAdapter

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

        itemListAdapter.onItemClick = {
            itemsModel.getInternship(it)
        }

        itemsModel = ViewModelProvider(this).get(InternshipViewModel::class.java)
        itemsModel.getInternships()

        itemsModel.internship.observe(viewLifecycleOwner, Observer { internship ->
            Log.v(TAG, "update items")
            if(internship != null)
                showInternshipsDialog(internship)
        })

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

    private lateinit var alertDialog: AlertDialog
    private fun showInternshipsDialog(internship : Internship) {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.internship_dialog_fragment, null)

        dialogView.findViewById<EditText>(R.id.titleField).setText(internship.title)
        dialogView.findViewById<EditText>(R.id.locationField).setText(internship.location)
        dialogView.findViewById<EditText>(R.id.descriptionField).setText(internship.description)
        dialogView.findViewById<EditText>(R.id.startDateField).setText(internship.startDate)
        dialogView.findViewById<EditText>(R.id.endDateField).setText(internship.endDate)

        val saveInternshipBtn: Button = dialogView.findViewById(R.id.saveInternshipBtn)
        val closeBtn: Button = dialogView.findViewById(R.id.closeBtn)
        val applyToInternshipBtn: Button = dialogView.findViewById(R.id.applyToInternshipBtn)
        saveInternshipBtn.isVisible = false

        applyToInternshipBtn.setOnClickListener {
            itemsModel.saveJobApplication(JobApplication(null, ApplicationStatus.PENDING, AppPreferences.currentUserId, internship.id!!))
            alertDialog.hide()
        }

        closeBtn.setOnClickListener{
            alertDialog.hide()
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun setupStudentsList(){
     //TODO: add students adapter
        studentListAdapter = StudentsListAdapter(this)
        item_list.adapter = studentListAdapter

        studentListAdapter.onItemClick = {
            showStudentDialog(it)
        }

        itemsModel = ViewModelProvider(this).get(InternshipViewModel::class.java)
//        itemsModel.getInternships()
//
//        itemsModel.internships.observe(viewLifecycleOwner, Observer { items ->
//            Log.v(TAG, "update items")
//            itemListAdapter.internshipsDTO = items
//        })
//
//        itemsModel.loading.observe(viewLifecycleOwner, Observer { loading ->
//            Log.v(TAG, "update loading")
//            progress.visibility = if (loading) View.VISIBLE else View.GONE
//        })
//
//        itemsModel.loadingError.observe(viewLifecycleOwner, Observer { exception ->
//            if (exception != null) {
//                Log.v(TAG, "update loading error")
//                val message = "Loading exception ${exception.message}"
//                val parentActivity = activity?.parent
//
//                if (parentActivity != null) {
//                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
    }

    private lateinit var applicationAlertDialog: AlertDialog
    private fun showStudentDialog(student: Student){
        //TODO: show internship and student details
    }
}