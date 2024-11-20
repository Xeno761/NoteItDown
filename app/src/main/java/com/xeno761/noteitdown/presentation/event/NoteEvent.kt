package com.xeno761.noteitdown.presentation.event

sealed interface NoteEvent {
    data class Load(val id: Long) : NoteEvent
    data class FolderChanged(val value: Long?) : NoteEvent
    data class Edit(val key: String, val value: String = "") : NoteEvent
    data object SwitchType : NoteEvent
    data object Delete : NoteEvent
    data object Save : NoteEvent
    data object Update : NoteEvent

    // New event for navigating back without saving
    data object NavigateBack : NoteEvent
}
