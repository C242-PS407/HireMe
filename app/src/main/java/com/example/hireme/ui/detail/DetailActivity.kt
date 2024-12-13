package com.example.hireme.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hireme.R
import com.example.hireme.data.local.model.Job
import com.example.hireme.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val job = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(DATA, Job::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(DATA)
        }

        if (job != null) {
            binding.apply {
                job.apply {
                    if (logo != null) {
                        detailImage.setImageResource(logo)
                    }
                    detailName.text = name
                    detailCompanyName.text = companyName
                    @SuppressLint("SetTextI18n")
                    detailLocationName.text = "$location • $jobType"
                    detailTimeTitle.text = jobTime
                    if (job.accessibilityStatus) {
                        detailAccessibilityName.apply {
                            setTextColor(getColor(R.color.emerald_600))
                            text = getString(R.string.accessible)
                            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                        }
                    } else {
                        detailAccessibilityName.apply {
                            setTextColor(getColor(R.color.neutral_600))
                            text = getString(R.string.not_accessible)
                            typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                        }
                    }
                    detailSalaryName.text = salary
                    detailDescriptionContent.text = description
                    detailRequirementContent.text = requirement
                    detailApply.setOnClickListener {
                        val applicationLink = Uri.parse(link)
                        val intent = Intent(Intent.ACTION_VIEW, applicationLink)
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA = "data"
    }
}