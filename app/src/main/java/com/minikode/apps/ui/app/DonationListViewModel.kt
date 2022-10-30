package com.minikode.apps.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.minikode.apps.entity.DonationEntity
import com.minikode.apps.repository.DonationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationListViewModel @Inject constructor(private val donationRepository: DonationRepository) :
    ViewModel() {

    private var _items: MutableLiveData<MutableList<DonationEntity>> = MutableLiveData(
        donationRepository.findDonation()
    )

    val items: LiveData<MutableList<DonationEntity>>
        get() = _items


    fun reload() = CoroutineScope(Dispatchers.Main).launch {
        _items.value = donationRepository.findDonation()
    }

    companion object {
        private const val TAG = "DonationListViewModel"
    }
}