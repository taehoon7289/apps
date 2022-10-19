package com.minikode.apps.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.minikode.apps.App
import com.minikode.apps.entity.LikeEntity
import com.minikode.apps.room.database.BaseDatabase
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AppInfoVo
import kotlinx.coroutines.*
import java.time.LocalDateTime

class LikeRepository {
    companion object {
        private const val TAG = "LikeRepository"
    }

    private val baseDb = BaseDatabase.getDatabase(App.instance)

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveLike(appInfoVo: AppInfoVo) {
        with(appInfoVo) {
            val likeEntity = LikeEntity(
                packageName = this.packageName,
                label = this.label,
                createDate = Util.getLocalDateTimeToString(
                    localDateTime = LocalDateTime.now()
                ),
            )
            CoroutineScope(Dispatchers.IO).launch {
                val likeNo = insertLike(likeEntity)
                updateLikeSeq(likeNo, likeNo.toInt())
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveLikes(appInfoVoList: MutableList<AppInfoVo>): MutableList<Long> = runBlocking {
        val likeNos = mutableListOf<Long>()
        for (appInfoVo in appInfoVoList) {
            with(appInfoVo) {
                val likeEntity = LikeEntity(
                    packageName = this.packageName,
                    label = this.label,
                    createDate = Util.getLocalDateTimeToString(
                        localDateTime = LocalDateTime.now()
                    ),
                )
                val likeNo = CoroutineScope(Dispatchers.IO).async {
                    val likeNo = insertLike(likeEntity)
                    updateLikeSeq(likeNo, likeNo.toInt())
                    likeNo
                }.await()
                likeNos.add(likeNo)
            }
        }
        likeNos
    }

    fun updateSeq(likeNo: Long, seq: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            updateLikeSeq(likeNo, seq)
        }
    }

    fun removeLike(appInfoVo: AppInfoVo) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteLike(appInfoVo.packageName!!)
        }
    }


    private suspend fun insertLike(likeEntity: LikeEntity) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().insert(likeEntity)
        }

    private suspend fun deleteLike(packageName: String) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().deleteByPackageName(
                packageName = packageName
            )
        }

    private suspend fun updateLikeSeq(likeNo: Long, seq: Int) =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().updateSeq(
                likeNo = likeNo,
                seq = seq,
            )
        }

}