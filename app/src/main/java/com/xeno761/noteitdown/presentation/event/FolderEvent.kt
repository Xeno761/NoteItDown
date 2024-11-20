package com.xeno761.noteitdown.presentation.event

import com.xeno761.noteitdown.data.local.entity.FolderEntity

sealed interface FolderEvent {

    data class AddFolder(val folder: FolderEntity) : FolderEvent

    data class UpdateFolder(val folder: FolderEntity) : FolderEvent

    data class DeleteFolder(val folder: FolderEntity) : FolderEvent
}