package com.xeno761.noteitdown.domain.repository

import com.xeno761.noteitdown.data.local.entity.FolderEntity
import kotlinx.coroutines.flow.Flow

interface FolderRepository {

    fun getAllFolders(): Flow<List<FolderEntity>>

    suspend fun insertFolder(folderEntity: FolderEntity)

    suspend fun deleteFolder(folderEntity: FolderEntity)

    suspend fun updateFolder(folderEntity: FolderEntity)

}