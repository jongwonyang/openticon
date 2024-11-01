package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.data.repository.EmoticonPacksRepository
import io.ssafy.openticon.domain.model.EmoticonPackDetail
import io.ssafy.openticon.domain.model.toEmoticonPackDetail
import javax.inject.Inject

class GetPublicPackDetailUseCase @Inject constructor(
    private val repository: EmoticonPacksRepository
) {
    suspend operator fun invoke(emoticonPackId: Int): Result<EmoticonPackDetail> {
        return try {
            val data = repository.getPublicPackInfo(emoticonPackId)
            Result.success(data.toEmoticonPackDetail())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}