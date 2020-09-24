package com.example.sun8.DataSource

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import androidx.paging.PageKeyedDataSource
import com.example.sun8.data.remote.Animal
import com.example.sun8.data.room.SunUser
import com.example.sun8.data.room.SunUserDataBase
import com.github.kittinunf.fuel.Fuel

/**
 * 这个地方定义的枚举类，跟静态常量类差不多
 */
enum class NetworkStatus {
    INITIAL_LOADING,
    LOADING,
    LOADED,
    FAILED,
    COMPLETED
}
/**
 * PageKeyedDataSource 两个参数，（第一个参数：页码 第二个参数，数据类型）
 * Hit 不是SunUser 是 animals.hits 所对用的类型
 */

class SunDataSource(private val context: Context) : PageKeyedDataSource<Int, SunUser>() {
    /**
     * 定义网络状态静态常量
     * 上面用的枚举类，所以这里注释一下，功能相同
     */
//    companion object{
//        const val LOADING = 1  // 加载中
//        const val FAILED = 2   //加载失败 failed
//        const val COMPLETED = 3 //completed 加载成功
//    }
    var sun_retry : (() -> Any)? = null
    private val _sun_NetworkStatus = MutableLiveData<NetworkStatus>()   //对内
    val sun_NetworkStatus : LiveData<NetworkStatus> = _sun_NetworkStatus  //对外

    /**
     * loadInitial 加载起始时，运行
     */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SunUser>
    ) {
        sun_retry = null
        _sun_NetworkStatus.postValue(NetworkStatus.LOADING)
        var xdikd = "";
        var names = arrayOf("flowers","tea","animal","plant","mountain");
        var vader = names.random()
        val httpAsync = Fuel.get("https://pixabay.com/api/?key=17946669-543fe6c4c313739ab33b63515&q="+vader+"&image_type=photo&pretty=true&per_page=10&page=1")
            .responseObject(Animal.Deserializer()) { request, response, result ->
                val(animals, err) = result   //Kotlin 写法
                //val animals = result.component1() //java写法
                if (animals != null) {
                    /**
                     * 把远程数据，写入数据库
                     */
                    var xdikd = animals.hits.toList()
                    callback.onResult(xdikd,null,2)
                    var i=1
                    val dao = SunUserDataBase.getInstance(context)?.getUserDao()
                    for (cursor in animals.hits){
                        //println("Jessice:"+i+"------"+cursor.largeImageURL)
                        dao?.insertData(SunUser(i, cursor.largeImageURL,cursor.webformatURL,cursor.webformatHeight))
                        //item = item.map(SunUser(i, cursor.largeImageURL))
                        i++
                    }
                    _sun_NetworkStatus.postValue(NetworkStatus.LOADING)
                    //println("Jessice:网络状态："+NetworkStatus.LOADING)
                    //println("Jessice:第一页")
                }else{
                    sun_retry = {loadInitial(params, callback)} //保存一下失败之后的原型函数
                    /**
                     * 网络出错
                     * 这里使用 postValue 不起作用，使用 value 有用
                     */
                    _sun_NetworkStatus.value = NetworkStatus.FAILED
                }
            }
    }

    /**
     * 往后一页
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SunUser>) {
        _sun_NetworkStatus.postValue(NetworkStatus.LOADING)
        sun_retry = null
        var xdikd = "";
        var names = arrayOf("flowers","tea","animal","plant","mountain");
        var vader = names.random()
        val httpAsync = Fuel.get("https://pixabay.com/api/?key=17946669-543fe6c4c313739ab33b63515&q="+vader+"&image_type=photo&pretty=true&per_page=10&page=${params.key}")
            .responseObject(Animal.Deserializer()) { request, response, result ->
                val(animals, err) = result   //Kotlin 写法
                //val animals = result.component1() //java写法
                if (animals != null) {
                    /**
                     * 把远程数据，写入数据库
                     */
                    //println("Jessice:网络状态：正常")
                    var xdikd = animals.hits.toList()
                    callback.onResult(xdikd,params.key+1) //下一页
                    var i=1
                    val dao = SunUserDataBase.getInstance(context)?.getUserDao()
                    for (cursor in animals.hits){
                        //println("Jessice:"+i+"------"+cursor.largeImageURL)
                        dao?.insertData(SunUser(i, cursor.largeImageURL,cursor.webformatURL,cursor.webformatHeight))
                        //item = item.map(SunUser(i, cursor.largeImageURL))
                        i++
                    }
                    _sun_NetworkStatus.postValue(NetworkStatus.LOADING)
                    //println("Jessice:第${params.key}页")
                }else{
                    sun_retry = {loadAfter(params, callback)} //保存一下失败之后的原型函数
                    /**
                     * 网络出错
                     * 这里使用 postValue 不起作用，使用 value 有用
                     */
                    //println("Jessice:网络状态-sun_retry："+sun_retry)
                    _sun_NetworkStatus.value = NetworkStatus.FAILED
                }
            }

    }

    /**
     * 往前一页，暂时用不到
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SunUser>) {
        TODO("Not yet implemented")
    }

}