package com.example.sun8.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * SunUser 实体类
 * @Entity()
 * 也可以写成
 * @Entity(tableName = "SunUser")
 */
@Entity(tableName = "SunUser")
@Parcelize data class SunUser (
    /**
     * 主键自动增加
     * 注意：主键，自动增加，不能使用 val 声明，可用 var
     */
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    @ColumnInfo(name="picture")
    var largeImageURL:String?=null,
    var webformatURL:String?=null,
    var webformatHeight:Int?=1
): Parcelable {
    constructor() : this(1, "111","a",1)
}