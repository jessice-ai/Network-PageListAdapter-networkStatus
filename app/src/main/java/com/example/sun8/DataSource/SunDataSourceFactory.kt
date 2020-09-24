package com.example.sun8.DataSource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.sun8.data.room.SunUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

/**
 * 创建一个 DataSourceFactory ，返回一个 DataSource 实例
 */
class SunDataSourceFactory(private val context:Context): DataSource.Factory<Int, SunUser>() {
    private val _sun_DataSource = MutableLiveData<SunDataSource>()
    val sun_DataSource : LiveData<SunDataSource> = _sun_DataSource
    //创建一个DataSource对象，并返回
    override fun create(): DataSource<Int, SunUser> {
        return SunDataSource(context).also { _sun_DataSource.postValue(it) }
    }

}