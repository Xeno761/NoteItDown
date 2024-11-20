package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.domain.repository.NoteRepository

class GetNoteById(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Long): NoteEntity? {
        return repository.getNoteById(id)
    }
}