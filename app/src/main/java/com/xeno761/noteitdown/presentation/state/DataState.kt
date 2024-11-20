package com.xeno761.noteitdown.presentation.state

import androidx.compose.runtime.Stable
import com.xeno761.noteitdown.data.local.entity.NoteEntity
import com.xeno761.noteitdown.domain.usecase.NoteOrder
import com.xeno761.noteitdown.domain.usecase.OrderType

@Stable
data class DataState(
    val notes: List<NoteEntity> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val filterTrash: Boolean = false,
    val filterFolder: Boolean = false,
    val folderId: Long? = null
)