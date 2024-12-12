package com.example.hireme.ui.profile

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hireme.R
import com.example.hireme.databinding.FragmentProfileBinding
import com.example.hireme.ui.ViewModelFactory

class ProfileFragment : Fragment() {

    private var currentCVUri: Uri? = null
    private var currentCVName: String? = null

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                binding.profileName.text = user.name
                binding.profileEmail.text = user.email
            }
        }

        viewModel.getCV().observe(viewLifecycleOwner) { cv ->
            currentCVName = cv.name
            currentCVUri = Uri.parse(cv.uri)
            showCV()
        }

        binding.filePickerButton.setOnClickListener {
            if (currentCVName == "") {
                openPdfPicker()
            } else {
                removePdf()
            }
        }
    }

    private fun removePdf() {
        viewModel.removeCV()
    }

    private fun openPdfPicker() {
        pickSingleDocument.launch(arrayOf("application/pdf"))
    }

    private val pickSingleDocument =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                val name = getDocumentName(it)
                currentCVName = name
                currentCVUri = it
                viewModel.addCV(name, it)
                showToast("$name is selected")
                showCV()
            } ?: run {
                showToast("No document is selected")
            }
        }

    private fun getDocumentName(uri: Uri): String {
        var name = "Unknown"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
        return name
    }

    private fun showCV() {
        currentCVName?.let {
            binding.filePickerEditText.setText(it)
        }
        if (currentCVName == "") {
            binding.filePickerButton.setImageResource(R.drawable.ic_outline_file_upload_24dp)
        } else {
            binding.filePickerButton.setImageResource(R.drawable.ic_baseline_close_24dp)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val PICK_PDF_FILE = 2
    }
}