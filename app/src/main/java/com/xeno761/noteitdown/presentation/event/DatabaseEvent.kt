package com.xeno761.noteitdown.presentation.event

import android.content.ContentResolver
import android.net.Uri
import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.presentation.component.dialog.ExportType

sealed interface DatabaseEvent {

    data class Import(
        val contentResolver: ContentResolver,
        val folderId: Long?,
        val uriList: List<Uri>
    ) : DatabaseEvent

    data class Export(
        val contentResolver: ContentResolver,
        val notes: List<NoteEntity>,
        val type: ExportType
    ) : DatabaseEvent

    data class Backup(val contentResolver: ContentResolver) : DatabaseEvent
    data class Recovery(val contentResolver: ContentResolver, val uri: Uri) : DatabaseEvent
    data object Reset : DatabaseEvent
}