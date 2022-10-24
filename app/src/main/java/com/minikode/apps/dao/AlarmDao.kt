package com.minikode.apps.dao

import androidx.room.*
import com.minikode.apps.entity.AlarmEntity

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(alarmEntity: AlarmEntity)

    @Delete
    fun delete(alarmEntity: AlarmEntity): Int

    @Query("DELETE FROM alarm WHERE alarmNo = :alarmNo")
    fun deleteById(alarmNo: Long): Int

    @Query("SELECT * FROM alarm ORDER BY alarmNo DESC")
    fun findAllAlarmNoDesc(): MutableList<AlarmEntity>

    @Query("DELETE FROM alarm WHERE requestCode = :requestCode")
    fun deleteByRequestCode(requestCode: Int): Int

    @Query("DELETE FROM alarm WHERE executeDate <= :executeDate")
    fun deleteByExecuteDateLessEqualThan(executeDate: Long): Int
}