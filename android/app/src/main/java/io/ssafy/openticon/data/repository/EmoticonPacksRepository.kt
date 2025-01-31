package io.ssafy.openticon.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ssafy.openticon.data.local.EmoticonDao
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import io.ssafy.openticon.data.model.EmoticonPackOrder
import io.ssafy.openticon.data.model.LikeEmoticon
import io.ssafy.openticon.data.model.PackInfoResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto
import io.ssafy.openticon.data.model.TagListResponseDto
import io.ssafy.openticon.data.remote.EmoticonPacksApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class EmoticonPacksRepository @Inject constructor(
    private val api: EmoticonPacksApi,
    private val emoticonDao: EmoticonDao,
    @ApplicationContext private val context: Context
) {
    suspend fun searchEmoticonPacks(
        query: String,
        type: String,
        sort: String,
        size: Int,
        page: Int
    ): PageEmoticonPackResponseDto {
        return api.searchEmoticonPacks(
            query = query,
            type = type,
            sort = sort,
            size = size,
            page = page
        )
    }

    suspend fun searchEmoticonTags() : TagListResponseDto{
        return api.getTagsInfo()
    }


    suspend fun searchEmoticonPackByImage(
        size: Int,
        page: Int,
        sort: String,
        @Part image: MultipartBody.Part
    ): PageEmoticonPackResponseDto {
        return api.imageSearchEmoticonPacks(size = size, page = page, sort = sort, image = image)
    }

    suspend fun getPublicPackInfo(emoticonPackId: Int): PackInfoResponseDto {
        return api.getPublicPackInfo(emoticonPackId)
    }

    suspend fun getPackInfo(uuid: String): PackInfoResponseDto {
        return api.getPackInfo(uuid)
    }

    suspend fun savedEmoticonPack(emoticonPackEntity: EmoticonPackEntity) {
        emoticonDao.insertEmoticonPack(emoticonPackEntity)
    }

    suspend fun downloadAndSavePublicEmoticonPack(packId: Int, emoticonUrls: List<String>) {
        for ((index, url) in emoticonUrls.withIndex()) {
            val fileName = "emoticon_$index.${url.substringAfterLast(".")}"
            val filePath = downloadAndSaveEmoticonFile(url, packId, fileName)

            if (filePath != null) {
                val emoticon = Emoticon(
                    id = "$packId-$index",
                    packId = packId,
                    filePath = filePath
                )
                emoticonDao.insertEmoticon(emoticon)
            } else {
                throw Exception("Failed to download emoticon from $url")
            }
        }
    }

    private suspend fun downloadAndSaveEmoticonFile(emoticonUrl: String, packId: Int, fileName: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val responseBody = api.downloadEmoticon(emoticonUrl)
                saveFileToLocal(responseBody, packId, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun saveFileToLocal(body: ResponseBody, packId: Int, fileName: String): String? {
        return try {
            val packDir = File(context.filesDir, "emoticon_packs/$packId")
            if (!packDir.exists()) packDir.mkdirs()

            val file = File(packDir, fileName)
            val inputStream = body.byteStream()
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getLocalEmoticonPacks(): Flow<List<EmoticonPackEntity>> {
        return emoticonDao.getAllEmoticonPacks()
    }

    suspend fun getDownloadedEmoticonPacks(): Flow<List<EmoticonPackEntity>> {
        return emoticonDao.getAllDownloadedEmoticonPacks()
    }

    suspend fun getEmotionsByPackId(packId: Int): Flow<List<Emoticon>>{
        return emoticonDao.getEmoticonsByPack(packId)
    }


    fun getPurchasedPackInfo(packId: Int): Flow<EmoticonPackEntity?> {
        return emoticonDao.getEmoticonPack(packId)
    }

    suspend fun updateEmoticonPack(emoticonPackEntity: EmoticonPackEntity){
        emoticonDao.updateEmoticonPack(emoticonPackEntity)
    }

    suspend fun getLikeEmoticonPack(): Flow<List<LikeEmoticon>> {
        return emoticonDao.getLikeEmoticonPack()
    }

    suspend fun deleteEmoticonsByPackId(packId: Int) {
        withContext(Dispatchers.IO) {
            emoticonDao.deleteEmoticonsByPackId(packId)
        }
    }

    suspend fun deleteLikeEmoticonsByPackId(packId: Int) {
        withContext(Dispatchers.IO) {
            emoticonDao.deleteLikeEmoticonsByPackId(packId)
        }
    }

    suspend fun insertLikeEmoticons(likeEmoticon: LikeEmoticon) {
        withContext(Dispatchers.IO) {
            emoticonDao.insertLikeEmoticon(likeEmoticon)
        }
    }

    suspend fun deleteFromOrder(packId: Int) {
        withContext(Dispatchers.IO) {
            emoticonDao.deleteFromOrder(packId)
        }
    }

    suspend fun deleteAllEmoticonPacksOrder(){
        withContext(Dispatchers.IO) {
            emoticonDao.deleteAllEmoticonPacksOrder()
        }
    }

    suspend fun deleteLikeEmoticonsById(id: Int){
        withContext(Dispatchers.IO) {
            emoticonDao.deleteLikeEmoticonsId(id)
        }
    }

    suspend fun insertOrder(packId: Int){
        withContext(Dispatchers.IO) {
            emoticonDao.insertPackOrder(EmoticonPackOrder(packId = packId))
        }
    }
}