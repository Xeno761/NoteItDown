package com.xeno761.noteitdown.domain.usecase

import com.xeno761.noteitdown.domain.repository.NoteRepository

class DeleteNotesByFolderId(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(folderId: Long?) {
        repository.deleteNotesByFolderId(folderId)
    }
}