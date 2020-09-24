package com.example.sun8.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.sun8.R
import com.example.sun8.adapter.SunPagerViewListAdapter
import com.example.sun8.data.room.SunUser
import kotlinx.android.synthetic.main.fragment_pager_view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PagerViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PagerViewFragment : Fragment() {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var adapterxd = SunPagerViewListAdapter()  //创建一个适配器

        val sunphotolist = arguments?.getParcelableArrayList<SunUser>("SUNA_LIST")
        adapterxd.apply {
            sunViewPager2.adapter = this
            submitList(sunphotolist)
        }
        /**
         * 图片滑动第一次改变，时候执行
         * position 第几张图片，自己会更新
         * position+1 是为不要从0开始
         */
        sunViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                suntextid.text = "${position+1}/${sunphotolist?.size}"
            }
        })
        /**
         * 图片竖向滚动只需下面一行，不设置，默认横向滚动
         */
        sunViewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        /**
         * 点击那个图片，模式从那个显示那个图片
         * 第二个参数默认，会有平滑滚动效果，不需要，设置false即可
         */
        sunViewPager2.setCurrentItem(arguments?.getInt("SUNA_POSITION")?:0,false)
        //suntextid

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_view, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PagerViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PagerViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}