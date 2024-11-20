package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.domain.repository.NoteRepository

class UpdateNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: NoteEntity) {
        repository.updateNote(note)
    }
}