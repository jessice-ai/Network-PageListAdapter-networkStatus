package com.example.sun8.repository

import androidx.lifecycle.LiveData
import com.example.sun8.data.remote.Animal
import com.example.sun8.data.room.SunUser
import com.example.sun8.data.room.SunUserDao
import com.example.sun8.data.room.SunUserDataBase
import com.github.kittinunf.fuel.Fuel


class SunRepository(private val SunUserDao: SunUserDao) {

    var database: SunUserDataBase?= null

    //    fun getAlldata(): List<SunUser>? {
//        //println("JessiceDao"+SunUserDao)
//        var xdun = SunUserDao?.getAll()
//        // println("Jessice---xxx---"+xdun)
//        return xdun
//    }
    fun getstrind():String{
        return "jessice:aaaa";
    }
    fun getmAllWords(): List<SunUser> {
        var xreturn : List<SunUser>
        xreturn = SunUserDao?.getmSmaillAllWords()
        return xreturn
    }
    fun deleteUserById(id: Int) {
        SunUserDao?.deleteUserById(id)
    }
    fun insert(SunUser:SunUser){
        SunUserDao?.insertData(SunUser)
    }


    /**
     * Fuel 使用 POST 方式获取远程数据，返回的是 Javabean，代码
     * 获取远程数据，并转化为数组
     */
    fun getremotedata(): String {
        var item: LiveData<List<SunUser>>? = null
        var xdikd = "";
        var names = arrayOf("flowers","tea","animal","plant","mountain");
        var vader = names.random()
        val httpAsync = Fuel.get("https://pixabay.com/api/?key=17946669-543fe6c4c313739ab33b63515&q="+vader+"&image_type=photo&pretty=true&per_page=200")
            .responseObject(Animal.Deserializer()) { request, response, result ->
                val(animals, err) = result   //Kotlin 写法
                //val animals = result.component1() //java写法
                if (animals != null) {
                    /**
                     * 把远程数据，写入数据库
                     */
                    var xdikd = animals.hits.toList()
                    var i=1
                    for (cursor in animals.hits){
                        //println("Jessice:"+i+"------"+cursor.largeImageURL)
                        insert(SunUser(i, cursor.largeImageURL,cursor.webformatURL,cursor.webformatHeight))
                        //item = item.map(SunUser(i, cursor.largeImageURL))
                        i++
                    }
                }
            }
        return xdikd
    }
}