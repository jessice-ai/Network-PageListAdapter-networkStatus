package com.example.sun8.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.sun8.R
import com.example.sun8.adapter.SunPageListAdapter
import com.example.sun8.data.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.fragment_one.*
import com.example.sun8.DataSource.NetworkStatus


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OneFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var sunMyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        /**
         * 对 模板 RecyclerView 进行管理 并切割成两列
         * sun_list_item_Recyclerview_id 是一个 RecyclerView 的ID
         */

        var adapterxd = SunPageListAdapter(sunMyViewModel)  //创建一个适配器
        sun_list_item_Recyclerview_id.apply {
            adapter = adapterxd
            // fragment 中 context 替换为 requireContext()
            //layoutManager = GridLayoutManager(context,2)  //2代替两列
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)  //不整齐两列
        }

        /**
         * 创建一个ViewModel 对象，并观察ViewModel 里面元素值是否变化
         */
        // 当 ViewModelProvider 显示红色虚线时，检查ViewModel 依赖是否添加

        // Observer 导入时，选择 androidx.lifecycle

        sunMyViewModel.sunPageListData.observe(viewLifecycleOwner, Observer { sunit->
            sunit?.let {
                println("Jessice:观察到数据发生变化，准备调用适配器")
                adapterxd.submitList(it)
                swipeRefresh.isRefreshing = false //数据加载完毕，关闭转动图标
            }
        })
        //sunMyViewModel.allWords.value?:sunMyViewModel.getmAllWords()
//        swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//            sunMyViewModel.getmAllWords()
//            swipeRefresh.setRefreshing(false);  //关闭下拉刷新动画
//        })
        swipeRefresh.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            sunMyViewModel.resetquery() //重新创建一个DataSource
        })
        /**
         * 观察 ViewModel 中网络状态字段
         */

        sunMyViewModel.sun_networkstatus.observe(viewLifecycleOwner, Observer {
            println("Jessice:网络状态TTTT："+it)
            adapterxd.updateNetworkStatus(it)
            swipeRefresh.isRefreshing = it == NetworkStatus.INITIAL_LOADING
        })
    }
    /**
     * Menubar 作用： 点击menu不同菜单，执行不同功能
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.sun_menu_titlebar -> {
                /**
                 * 重新链接网络
                 */
                var sunMyViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
                sunMyViewModel.sun_retry()
                swipeRefresh.isRefreshing = true
                Handler().postDelayed( {sunMyViewModel.sun_retry() },1000)
                true
            }
            R.id.sun_game -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OneFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}