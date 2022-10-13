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
    var executeDate: String? = null,
    var createDate: String? = null,
    var periodType: String? = null,

    )