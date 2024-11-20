package com.xeno761.noteitdown.presentation.event

import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.domain.usecase.NoteOrder
import com.xeno761.noteitdown.domain.usecase.OrderType

sealed interface ListEvent {

    data class Noteitdown(val noteEntity: NoteEntity) : ListEvent
    data class Search(val key: String) : ListEvent

    data class Sort(
        val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        val filterFolder: Boolean = false,
        val folderId: Long? = null,
        val trash: Boolean = false
    ) : ListEvent

    data class DeleteNotes(val noteEntities: Collection<NoteEntity>, val recycle: Boolean) : ListEvent
    data class MoveNotes(val noteEntities: Collection<NoteEntity>, val folderId: Long?) : ListEvent
    data class RestoreNotes(val noteEntities: Collection<NoteEntity>) : ListEvent

    data object ToggleOrderSection : ListEvent
    data object ChangeViewMode : ListEvent
    data object AddNote : ListEvent
}