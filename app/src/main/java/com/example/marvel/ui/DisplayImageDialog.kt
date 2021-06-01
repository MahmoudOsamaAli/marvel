package com.example.marvel.ui

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.marvel.R
import com.example.marvel.databinding.FragmentDisplayImageBinding
import com.example.marvel.utils.Extensions.setNoLimitsWindow
import com.example.marvel.viewModels.CharacterDetailsViewModel

class DisplayImageDialog : DialogFragment() {

    private lateinit var binding: FragmentDisplayImageBinding
    private lateinit var viewModel: CharacterDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_display_image, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[CharacterDetailsViewModel::class.java]
        viewModel.selectedItemThumbnailLiveData.observe(viewLifecycleOwner,
            { data -> setImage(data) })
    }

    private fun setImage(url: String) {
        if (!url.contains("image_not_available")) {
            Glide.with(requireContext()).load(Uri.parse(url)).into(binding.zoomageImageView)
        } else {
            Glide.with(requireContext()).load(R.drawable.image_placeholder)
                .into(binding.zoomageImageView)
        }
    }

    override fun onStart() {
        super.onStart()
        setWindowParams()
    }

    private fun setWindowParams() {
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.black_fade
                )
            )
        )
        dialog?.window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        setNoLimitsWindow()
    }
}