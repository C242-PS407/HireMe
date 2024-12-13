package com.example.hireme.ui.jobs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hireme.R
import com.example.hireme.data.local.model.Job
import com.example.hireme.databinding.FragmentJobsBinding
import com.example.hireme.ui.JobsAdapter
import com.example.hireme.ui.ViewModelFactory
import com.example.hireme.ui.detail.DetailActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class JobsFragment : Fragment() {
    private val viewModel by viewModels<JobsViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var binding: FragmentJobsBinding

    private val list : MutableList<Job> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCV().observe(viewLifecycleOwner) { cv ->
            if (cv.name == "") {
                showEmptyState()
            } else {
                showJobs()
            }
        }

        binding.rvJobs.setHasFixedSize(true)

        list.clear()
        list.addAll(getFeaturedJobList())
        showRecyclerList()

        binding.jobsButtonEmpty.setOnClickListener {
            (requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                R.id.navigation_profile
        }
    }

    private fun showEmptyState() {
        binding.rvJobs.visibility = View.GONE
        binding.jobsRecommendedTitle.visibility = View.GONE
        binding.jobsEmpty.visibility = View.VISIBLE
    }

    private fun showJobs() {
        binding.rvJobs.visibility = View.VISIBLE
        binding.jobsRecommendedTitle.visibility = View.VISIBLE
        binding.jobsEmpty.visibility = View.GONE
    }

    @SuppressLint("Recycle")
    private fun getFeaturedJobList(): List<Job> {
        val dataLogo = resources.obtainTypedArray(R.array.data_logo)
        val dataName = resources.getStringArray(R.array.data_name)
        val dataCompany= resources.getStringArray(R.array.data_company)
        val dataLocation = resources.getStringArray(R.array.data_location)
        val dataJobType = resources.getStringArray(R.array.data_job_type)
        val dataJobTime = resources.getStringArray(R.array.data_job_time)
        val dataAccessibilityStatus = resources.getStringArray(R.array.data_accessibility_status)
        val dataSalary = resources.getStringArray(R.array.data_salary)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataRequirement = resources.getStringArray(R.array.data_requirement)
        val dataLink = resources.getStringArray(R.array.data_link)

        val dataSize = dataName.size
        val jobs = mutableListOf<Job>()

        for (i in 0 until dataSize) {
            val job = Job(
                dataLogo.getResourceId(i, 0),
                dataName[i],
                dataCompany[i],
                dataLocation[i],
                dataJobType[i],
                dataJobTime[i],
                dataAccessibilityStatus[i].toBooleanStrict(),
                dataSalary[i],
                dataDescription[i],
                dataRequirement[i],
                dataLink[i]
            )
            jobs.add(job)
        }

        return jobs
    }

    private fun showRecyclerList() {
        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        val jobListAdapter = JobsAdapter(requireContext(), list) { job: Job ->
            val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
            intentToDetail.putExtra(DetailActivity.DATA, job)
            startActivity(intentToDetail)
        }
        binding.rvJobs.adapter = jobListAdapter
    }
}