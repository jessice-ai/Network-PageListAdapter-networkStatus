package com.example.sun8.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sun8.R
import com.example.sun8.data.room.SunUser
import kotlinx.android.synthetic.main.sun_cell_content.view.*


class SunPagerViewListAdapter : ListAdapter<SunUser, SunPagerViewListAdapter.SunPagerViewHolder>(DIFFCALLBACK) {

    class SunPagerViewHolder(var sunHolder_View : View) : RecyclerView.ViewHolder(sunHolder_View){

    }
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SunPagerViewHolder {
        /**
         * onCreateViewHolder 作用： 加载一个视图出来，并创建一个 viewHolder，而viewHolder作为recyclerView缓存管理的对象
         * 可复用
         * 负责承载每个子项的布局
         */
        val holder = SunPagerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.sun_cell_content,parent,false))
        //println("Jessice-Sun-onCreateViewHolder----"+holder.getAdapterPosition());  // 打印一下 onCreateViewHolder 运行次数，recyclerView 可缓存
        return holder
    }
    /**
     * onBindViewHolder 为每一项赋值
     * 负责将每个子项 holder 绑定数据
     * 运行多次
     */
    override fun onBindViewHolder(holder: SunPagerViewHolder, position: Int) {
        //打印出所有的图片地址
        //println("Jessice:Picture----"+getItem(position).picture)
        //println("Jessice-Sun-onBindViewHolder————"+position);
        var imgUrl = getItem(position).webformatURL
        /**
         * 使用Glide 加载图片赋值给 holder.itemView.imageView3
         */
        Glide.with(holder.itemView)
            .load(imgUrl)
            .into(holder.itemView.imageView4)
    }
    object DIFFCALLBACK: DiffUtil.ItemCallback<SunUser>(){
        override fun areItemsTheSame(oldItem: SunUser, newItem: SunUser): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SunUser, newItem: SunUser): Boolean {
            return oldItem.id == newItem.id
        }
    }
}