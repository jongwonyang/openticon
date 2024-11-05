package io.ssafy.openticon.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmoticonDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmoticonPack(emoticonPackEntity: EmoticonPackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoticons(emoticons: List<Emoticon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmoticon(emoticon: Emoticon)

    @Query("SELECT * FROM emoticon_packs WHERE id = :packId")
    fun getEmoticonPack(packId: Int): Flow<EmoticonPackEntity?>

    @Query("SELECT * FROM emoticons WHERE packId = :packId")
    fun getEmoticonsByPack(packId: Int): Flow<List<Emoticon>>

    @Query("SELECT * FROM emoticon_packs")
    fun getAllEmoticonPacks(): Flow<List<EmoticonPackEntity>>

    @Query("UPDATE emoticon_packs SET downloaded = :b WHERE id = :packId")
    fun updateEmoticonPackDownloaded(packId: Int, b: Boolean)
}