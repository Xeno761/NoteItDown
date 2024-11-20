package com.xeno761.noteitdown.domain.usecase

sealed class OrderType {
    data object Ascending: OrderType()
    data object Descending: OrderType()
}