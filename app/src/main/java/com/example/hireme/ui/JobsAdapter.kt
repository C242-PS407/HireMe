package com.example.hireme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hireme.R
import com.example.hireme.data.local.model.Job
import com.example.hireme.databinding.ItemJobBinding

class JobsAdapter(
    private val context: Context,
    private val jobList: List<Job>,
    private val onClick: (Job) -> Unit
) : RecyclerView.Adapter<JobsAdapter.ListViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val job = jobList[position]
        holder.bind(job)

        holder.binding.apply {
            job.logo?.let { itemJobLogo.setImageResource(it) }
            itemJobTitle.text = job.name
            itemJobLocation.text = "${job.companyName} • ${job.location}"
            itemJobSalary.text = job.salary
            itemJobDescription.text = job.description
            itemJobTypeText.text = job.jobType
            if (job.accessibilityStatus) {
                itemJobAccessibilityText.apply {
                    text = context.getString(R.string.accessible)
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    setTextColor(context.getColor(R.color.emerald_600))
                }
            } else {
                itemJobAccessibilityText.apply {
                    setTextColor(context.getColor(R.color.neutral_600))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    text = context.getString(R.string.not_accessible)
                }
            }
            itemJobTimeText.text = job.jobTime
        }
    }

    override fun getItemCount(): Int = jobList.size

    inner class ListViewHolder(var binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(job: Job) {
            itemView.setOnClickListener {
                onClick(job)
            }
        }
    }
}