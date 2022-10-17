package com.minikode.apps.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "like")
class LikeEntity(
    @PrimaryKey(autoGenerate = true)
    var likeNo: Long? = null,
    var packageName: String? = null,
    var label: String? = null,
    var createDate: String? = null,
    var seq: Int? = null,

    )