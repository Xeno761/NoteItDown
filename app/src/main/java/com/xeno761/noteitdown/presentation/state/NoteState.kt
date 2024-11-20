package com.xeno761.noteitdown.presentation.state

import androidx.compose.runtime.Stable

@Stable
data class NoteState(
    val id: Long? = null,
    val folderId: Long? = null,
    val isMarkdown: Boolean = true,
    val timestamp: Long? = null
)