package com.xeno761.noteitdown.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun putFloat(key: String, value: Float)
    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun putStringSet(key: String, value: Set<String>)

    fun intFlow(key: String): Flow<Int>
    fun floatFlow(key: String): Flow<Float>
    fun stringFlow(key: String): Flow<String>
    fun booleanFlow(key: String): Flow<Boolean>
    fun stringSetFlow(key: String): Flow<Set<String>>
}