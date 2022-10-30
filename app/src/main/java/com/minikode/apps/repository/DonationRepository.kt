package com.minikode.apps.repository

import com.android.billingclient.api.Purchase
import com.minikode.apps.App
import com.minikode.apps.entity.DonationEntity
import com.minikode.apps.room.database.BaseDatabase
import kotlinx.coroutines.*
import java.util.*

class DonationRepository {
    companion object {
        private const val TAG = "DonationRepository"
    }

    private val baseDb = BaseDatabase.getDatabase(App.instance)

    fun saveDonation(purchases: List<Purchase>) {
        for (purchase in purchases) {
            with(purchase) {
                val donationEntity = DonationEntity(
                    orderId = orderId,
                    purchaseState = purchaseState,
                    purchaseTime = purchaseTime,
                    purchaseToken = purchaseToken,
                    packageName = packageName,
                    developerPayload = developerPayload,
                    quantity = quantity,
                    signature = signature,
                    createDate = Calendar.getInstance().timeInMillis,
                    products = products.joinToString(","),
                )
                CoroutineScope(Dispatchers.IO).launch {
                    insertDonation(donationEntity)
                }
            }
        }
    }

    fun removeDonation(orderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteDonation(orderId)
        }
    }

    fun findDonation() = runBlocking {
        selectDonation()
    }

    fun findCountDonation() = runBlocking {
        selectDonationCount()
    }


    private suspend fun insertDonation(donationEntity: DonationEntity) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.donationDao().insert(donationEntity)
        }

    private suspend fun deleteDonation(orderId: String) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.donationDao().deleteByOrderId(
                orderId = orderId
            )
        }

    private suspend fun selectDonationCount() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.donationDao().count()
        }

    private suspend fun selectDonation(): MutableList<DonationEntity> =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.donationDao().findAllDonationNoDesc()
        }
}