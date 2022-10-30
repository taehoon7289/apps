package com.minikode.apps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "donation")
class DonationEntity(
    @PrimaryKey(autoGenerate = true)
    var donationNo: Long? = null,
    var orderId: String? = null,
    var purchaseState: Int? = null,
    var purchaseTime: Long? = null,
    var purchaseToken: String? = null,
    var packageName: String? = null,
    var developerPayload: String? = null,
    var quantity: Int? = null,
    var signature: String? = null,
    var createDate: Long? = null,
    var products: String? = null,

)