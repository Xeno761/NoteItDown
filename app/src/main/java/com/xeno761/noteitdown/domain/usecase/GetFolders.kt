package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.data.local.entity.FolderEntity
import com.xeno761.noteitdown.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow

class GetFolders(
    private val repository: FolderRepository
) {

    operator fun invoke(): Flow<List<FolderEntity>> =
        repository.getAllFolders()
}
