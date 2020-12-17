package com.kotlinapp.students

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kotlinapp.R
import com.kotlinapp.model.InternshipDTO
import com.kotlinapp.model.Student
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.students_view.view.*


class StudentsListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<StudentsListAdapter.ViewHolder>() {

    var onItemClick : ((Student) -> Unit)? = null

    var students = emptyList<Student>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.students_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemCount > 0) {
            Log.d(TAG, "Size elements: $itemCount")
            val student = students[position]
            //Set current user color
            if(position % 2 == 0)
                holder.layout.setBackgroundColor(Color.parseColor("#8C6A6868"))
            else
                holder.layout.setBackgroundColor(Color.parseColor("#009E9D9D"))

            holder.internshipTitle.text = student.firstName
            holder.companyName.text = student.lastName
            holder.itemView.tag = student
        }else{
            Toast.makeText(this.fragment.context,"No internships available yet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = students.size

    // recycler format
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: ConstraintLayout = view.findViewById(R.id.studentsLayout)
        val companyName: TextView = view.firstName
        val internshipTitle: TextView = view.lastName
        init {
            layout.setOnClickListener{
                onItemClick?.invoke(students[adapterPosition])
            }
        }
    }
}
