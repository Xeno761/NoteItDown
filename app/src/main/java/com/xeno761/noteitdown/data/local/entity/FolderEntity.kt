package com.xeno761.noteitdown.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xeno761.noteitdown.presentation.theme.*

@Entity
data class FolderEntity(
    @PrimaryKey val id: Long? = null,
    val name: String = "",
    val color: Int? = null
) {
    companion object {
        val folderColors = listOf(
            Red,
            Orange,
            Yellow,
            Green,
            Cyan,
            Blue,
            Purple
        )
    }
}