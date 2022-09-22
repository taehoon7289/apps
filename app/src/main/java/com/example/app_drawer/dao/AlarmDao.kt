package com.example.app_drawer.dao

import androidx.room.*
import com.example.app_drawer.entity.AlarmEntity

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

}