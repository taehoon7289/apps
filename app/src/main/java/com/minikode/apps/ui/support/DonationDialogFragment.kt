package com.minikode.apps.ui.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentDonationDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonationDialogFragment(
    private val clickCallback: () -> Unit
) : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentDonationDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_donation_dialog, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this@DonationDialogFragment
        initView()
    }

    private fun initView() {
        with(binding) {
            binding.linearLayoutDonation.setOnClickListener {
                clickCallback()
            }
        }
    }

    companion object {
        private const val TAG = "DonationDialogFragment"
    }
}