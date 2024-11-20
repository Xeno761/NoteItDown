package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val repository: NoteRepository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        trash: Boolean = false,
        filterFolder: Boolean = false,
        folderId: Long? = null,
    ): Flow<List<NoteEntity>> {

        val flow: Flow<List<NoteEntity>> = if (!trash) {
            if (filterFolder) {
                repository.getNotesByFolderId(folderId)
            } else{
                repository.getAllNotes()
            }
        } else {
            repository.getAllDeletedNotes()
        }

        return flow.map { notes ->
            when (noteOrder.orderType) {

                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.reversed()
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes
                    }
                }
            }
        }
    }
}
