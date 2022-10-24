package com.minikode.apps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm")
class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    var alarmNo: Long? = null,
    var requestCode: Int? = null,
    var packageName: String? = null,
    var label: String? = null,
    var executeDate: Long? = null,
    var createDate: Long? = null,
    var periodType: String? = null,
)