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
import com.kotlinapp.model.ApplicationDTO
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.applications_view.view.*


class StudentsListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<StudentsListAdapter.ViewHolder>() {

    var onItemClick : ((ApplicationDTO) -> Unit)? = null

    var applicationsDto = emptyList<ApplicationDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.applications_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemCount > 0) {
            Log.d(TAG, "Size elements: $itemCount")
            val applicationDTO = applicationsDto[position]
            //Set current user color
            if(position % 2 == 0)
                holder.layout.setBackgroundColor(Color.parseColor("#8C6A6868"))
            else
                holder.layout.setBackgroundColor(Color.parseColor("#009E9D9D"))

            holder.internshipTitle.text = applicationDTO.title
            holder.firstName.text = applicationDTO.firstName
            holder.lastName.text = applicationDTO.lastName
            holder.status.text = applicationDTO.status

            holder.itemView.tag = applicationDTO
        }else{
            Toast.makeText(this.fragment.context,"No internships available yet", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = applicationsDto.size

    // recycler format
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: ConstraintLayout = view.findViewById(R.id.studentsLayout)
        val firstName: TextView = view.firstName
        val lastName : TextView = view.lastName
        val status : TextView = view.status
        val internshipTitle: TextView = view.internshipTitle

        init {
            layout.setOnClickListener{
                onItemClick?.invoke(applicationsDto[adapterPosition])
            }
        }
    }
}
