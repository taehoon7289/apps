package com.minikode.apps.dao

import androidx.room.*
import com.minikode.apps.entity.DonationEntity

@Dao
interface DonationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(donationEntity: DonationEntity): Long

    @Delete
    fun delete(donationEntity: DonationEntity): Int

    @Query("DELETE FROM `donation` WHERE donationNo = :donationNo")
    fun deleteById(donationNo: Long): Int

    @Query("DELETE FROM `donation` WHERE orderId = :orderId")
    fun deleteByOrderId(orderId: String): Int

    @Query("SELECT * FROM `donation` ORDER BY donationNo DESC")
    fun findAllDonationNoDesc(): MutableList<DonationEntity>

    @Query("SELECT COUNT(*) FROM `donation`")
    fun count(): Long

}