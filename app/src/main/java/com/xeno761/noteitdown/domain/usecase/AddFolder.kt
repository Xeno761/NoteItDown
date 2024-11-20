package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.data.local.entity.FolderEntity
import com.xeno761.noteitdown.domain.repository.FolderRepository

class AddFolder(
    private val repository: FolderRepository
) {

    suspend operator fun invoke(folderEntity: FolderEntity) {
        repository.insertFolder(folderEntity)
    }
}