package com.example.app_drawer.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    var requestCode: Int? = null,
    var packageName: String? = null,
    var label: String? = null,
    var executeDate: LocalDateTime? = null,

    )