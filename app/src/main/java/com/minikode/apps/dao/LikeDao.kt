package com.minikode.apps.dao

import androidx.room.*
import com.minikode.apps.entity.LikeEntity

@Dao
interface LikeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(likeEntity: LikeEntity)

    @Delete
    fun delete(likeEntity: LikeEntity): Int

    @Query("DELETE FROM `like` WHERE likeNo = :likeNo")
    fun deleteById(likeNo: Long): Int

    @Query("DELETE FROM `like` WHERE packageName = :packageName")
    fun deleteByPackageName(packageName: String): Int

    @Query("SELECT * FROM `like` ORDER BY likeNo DESC")
    fun findAllLikeNoDesc(): MutableList<LikeEntity>

}