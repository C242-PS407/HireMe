package com.example.hireme.data.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Job (
    val logo: Int? = null,
    val name: String,
    val companyName: String,
    val location: String,
    val jobType: String,
    val jobTime: String,
    val accessibilityStatus: Boolean,
    val salary: String,
    val description: String,
    val requirement: String,
    val link: String
) : Parcelable