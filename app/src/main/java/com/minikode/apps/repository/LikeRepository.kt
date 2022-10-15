package com.minikode.apps.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.minikode.apps.App
import com.minikode.apps.entity.LikeEntity
import com.minikode.apps.room.database.BaseDatabase
import com.minikode.apps.util.Util
import com.minikode.apps.vo.AppInfoVo
import com.minikode.apps.vo.LikeInfoVo
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.*

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
                insertLike(likeEntity)
            }
        }

    }

    fun removeLike(appInfoVo: AppInfoVo) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteLike(appInfoVo.packageName!!)
        }
    }

    fun getItems(): MutableList<LikeInfoVo> = runBlocking {
        val likeEntities = selectLike()
        Log.d(TAG, "getItems: likeEntities $likeEntities")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return@runBlocking likeEntities.map {
                val packageName = it.packageName!!
                val packageManager = App.instance.packageManager
                val iconDrawable = packageManager.getApplicationIcon(packageName)
                val label =
                    packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager)

                val likeInfoVo = LikeInfoVo(
                    likeNo = it.likeNo,
                    createDate = Util.getStringToLocalDateTime(str = it.createDate!!),
                    packageName = packageName,
                    iconDrawable = iconDrawable,
                    label = label.toString(),
                )
                likeInfoVo
            }.toMutableList()
        }
        return@runBlocking mutableListOf<LikeInfoVo>()
    }

    private suspend fun selectLike() =
        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            baseDb.likeDao().findAllLikeNoDesc()
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

}