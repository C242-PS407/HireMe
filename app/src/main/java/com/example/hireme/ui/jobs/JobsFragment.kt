package com.example.hireme.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.hireme.databinding.FragmentJobsBinding

class JobsFragment : Fragment() {

    private lateinit var binding: FragmentJobsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val jobsViewModel =
            ViewModelProvider(this)[JobsViewModel::class.java]

        binding = FragmentJobsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
}