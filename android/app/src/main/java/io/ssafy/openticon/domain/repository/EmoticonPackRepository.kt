package io.ssafy.openticon.domain.repository

import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.domain.model.EmoticonPack

interface EmoticonPackRepository {
    suspend fun getPublicPackInfo(packId: Int): EmoticonPack
    suspend fun savePackInfo(emoticonPack: EmoticonPack)
    suspend fun downloadAndSavePublicEmoticonPack(packId: Int, emoticonUrls: List<String>)
    suspend fun updateDownloadedStatus(packId: Int, isDownloaded: Boolean)
    suspend fun deleteFilesFromId(packId: Int, emoticons: List<Emoticon>)
}