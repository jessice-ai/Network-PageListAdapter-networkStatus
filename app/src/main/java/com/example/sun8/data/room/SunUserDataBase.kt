package com.example.sun8.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * entities 映射 SunUser 实体类
 * version 指明当前数据库的版本号
 * companion object 单例模式，返回Database，以防止新建过多的实例造成内存的浪费
 */
@Database(version = 1, entities = [SunUser::class])  //生命实体类（Entity）的名字 User 为跟数据库的版本号
abstract class SunUserDataBase : RoomDatabase() {
    abstract fun getUserDao(): SunUserDao //创建DAO的抽象类
    companion object {
        private var INSTANCE: SunUserDataBase? = null  //创建单例
        fun getInstance(context: Context): SunUserDataBase? {
            if (INSTANCE == null) {
                /**
                 * Room.databaseBuilder(context,klass,name).build()来创建Database
                 * 第一个参数 context：上下文
                 * 第二个参数 为当前Database的class字节码文件
                 * 第三个参数为数据库名称
                 * allowMainThreadQueries() #加上这个方法是允许 Room 在主线程上操作，默认是拒绝的，因为操作数据库都还算是比较耗时的动作
                 * 测试的时候，可以加上，正式的时候去掉，直接去掉这个方法
                 */
                INSTANCE = Room.databaseBuilder(
                    context,
                    SunUserDataBase::class.java,
                    DATABASE_NAME
                ).allowMainThreadQueries().build()
                /**
                 * 数据库升级和降级
                 * 目的，是修改表结构，这很正常，经常会用到
                 * 比如：数据库从版本1升级到版本2，并在版本2上增加了age一列
                 * 使用 addMigrations 函数实现 数据升级跟降级，
                 * addMigrations 的 Migration 参数说明：
                 * 1、Migration 有两个参数，第一个参数：数据库老版本号；第二个参数，数据库新版本号
                 * 2、同时将 @Database注解中的version的值 修改为新数据库的版本号
                 * 3、database.execSQL 中包含 修改表的 SQL 语句
                 */
//                INSTANCE = Room.databaseBuilder(
//                    context,
//                    SunUserDataBase::class.java,
//                    DATABASE_NAME
//                ).addMigrations(object : Migration(1,2){
//                    override fun migrate(database: SupportSQLiteDatabase) {
//                        database.execSQL("ALTER TABLE user ADD age INTEGER Default 0 not null ")
//                    }
//
//                }).build()
            }
            /**
             * 返回数据库
             */
            return INSTANCE
        }
        fun destroyInstance() {
            INSTANCE = null
        }
        private const val DATABASE_NAME = "sunuserdeedf.db"
    }
}