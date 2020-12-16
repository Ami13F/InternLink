package com.kotlinapp.internships

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
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.internship_view.view.*


class InternshipDTOListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<InternshipDTOListAdapter.ViewHolder>() {

    var internshipsDTO = emptyList<InternshipDTO>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.internship_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (itemCount > 0) {
            Log.d(TAG, "Size elements: $itemCount")
            val internship = internshipsDTO[position]
            //Set current user color
            if (position % 2 == 0)
                holder.layout.setBackgroundColor(Color.parseColor("#8C6A6868"))
            else
                holder.layout.setBackgroundColor(Color.parseColor("#009E9D9D"))
            holder.internshipTitle.text = internship.internshipTitle
            holder.companyName.text = internship.companyName
            holder.itemView.tag = internship
        } else {
            Toast.makeText(
                this.fragment.context,
                "No internships available yet",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount() = internshipsDTO.size

    // recycler format
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layout: ConstraintLayout = view.findViewById(R.id.leaderLayout)
        val companyName: TextView = view.companyName
        val internshipTitle: TextView = view.internshipTitle
    }
}
