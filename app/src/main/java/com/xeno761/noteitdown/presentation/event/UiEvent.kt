package com.xeno761.noteitdown.presentation.event

sealed interface UiEvent {
    data object NavigateBack : UiEvent
}