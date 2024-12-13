package com.example.hireme.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hireme.R
import com.example.hireme.data.local.model.Job
import com.example.hireme.databinding.FragmentHomeBinding
import com.example.hireme.ui.JobsAdapter
import com.example.hireme.ui.ViewModelFactory
import com.example.hireme.ui.detail.DetailActivity
import com.example.hireme.ui.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var binding: FragmentHomeBinding

    private val featuredList : MutableList<Job> = mutableListOf()
    private val recommendedList : MutableList<Job> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCV().observe(viewLifecycleOwner) { cv ->
            if (cv.name == "") {
                hideRecommendedJobs()
            } else {
                showRecommendedJobs()
            }
        }

        binding.rvFeaturedJobs.setHasFixedSize(true)
        binding.rvRecommendedJobs.setHasFixedSize(true)

        featuredList.clear()
        recommendedList.clear()
        featuredList.addAll(getFeaturedJobList(listOf(2, 3, 0)))
        recommendedList.addAll(getFeaturedJobList(listOf(0, 1, 2)))
        showRecyclerList()

        binding.homeGreetingButtonEmpty.setOnClickListener {
            (requireActivity().findViewById<View>(R.id.nav_view) as BottomNavigationView).selectedItemId =
                R.id.navigation_profile
        }
    }

    private fun hideRecommendedJobs() {
        binding.rvRecommendedJobs.visibility = View.GONE
        binding.homeRecommendedTitle.visibility = View.GONE
        binding.homeGreetingButtonEmpty.visibility = View.VISIBLE
    }

    private fun showRecommendedJobs() {
        binding.rvRecommendedJobs.visibility = View.VISIBLE
        binding.homeRecommendedTitle.visibility = View.VISIBLE
        binding.homeGreetingButtonEmpty.visibility = View.GONE
    }

    @SuppressLint("Recycle")
    private fun getFeaturedJobList(specificIndices: List<Int>): List<Job> {
        val dataLogo = resources.obtainTypedArray(R.array.data_logo)
        val dataName = resources.getStringArray(R.array.data_name)
        val dataCompany = resources.getStringArray(R.array.data_company)
        val dataLocation = resources.getStringArray(R.array.data_location)
        val dataJobType = resources.getStringArray(R.array.data_job_type)
        val dataJobTime = resources.getStringArray(R.array.data_job_time)
        val dataAccessibilityStatus = resources.getStringArray(R.array.data_accessibility_status)
        val dataSalary = resources.getStringArray(R.array.data_salary)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataRequirement = resources.getStringArray(R.array.data_requirement)
        val dataLink = resources.getStringArray(R.array.data_link)

        val jobs = mutableListOf<Job>()

        for (index in specificIndices) {
            if (index < dataName.size) {
                val job = Job(
                    dataLogo.getResourceId(index, 0),
                    dataName[index],
                    dataCompany[index],
                    dataLocation[index],
                    dataJobType[index],
                    dataJobTime[index],
                    dataAccessibilityStatus[index].toBooleanStrict(),
                    dataSalary[index],
                    dataDescription[index],
                    dataRequirement[index],
                    dataLink[index]
                )
                jobs.add(job)
            }
        }
        return jobs
    }

    private fun showRecyclerList() {
        binding.rvFeaturedJobs.layoutManager = LinearLayoutManager(requireContext())
        val featuredJobListAdapter = JobsAdapter(requireContext(), featuredList) { job: Job ->
            val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
            intentToDetail.putExtra(DetailActivity.DATA, job)
            startActivity(intentToDetail)
        }
        binding.rvFeaturedJobs.adapter = featuredJobListAdapter


        binding.rvRecommendedJobs.layoutManager = LinearLayoutManager(requireContext())
        val recommendedJobListAdapter = JobsAdapter(requireContext(), recommendedList) { job: Job ->
            val intentToDetail = Intent(requireContext(), DetailActivity::class.java)
            intentToDetail.putExtra(DetailActivity.DATA, job)
            startActivity(intentToDetail)
        }
        binding.rvRecommendedJobs.adapter = recommendedJobListAdapter
    }
}