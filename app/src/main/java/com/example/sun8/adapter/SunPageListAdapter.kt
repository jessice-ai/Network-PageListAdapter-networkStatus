package com.example.sun8.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sun8.DataSource.NetworkStatus
import com.example.sun8.R
import com.example.sun8.data.room.SunUser
import com.example.sun8.data.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.sun_cell.view.*
import kotlinx.android.synthetic.main.sun_footer.view.*


class SunPageListAdapter(private val sunMyViewModel: MyViewModel) : PagedListAdapter<SunUser, RecyclerView.ViewHolder>(DIFFCALLBACK) {
    private var networkStatus: NetworkStatus? = null
    private var hasFooter = false
    init {
        //sunMyViewModel.sun_retry()
    }
    fun updateNetworkStatus(networkStatus: NetworkStatus?) {
        this.networkStatus = networkStatus
        if (networkStatus == NetworkStatus.INITIAL_LOADING) hideFooter() else showFooter()
    }
    private fun hideFooter() {
        if (hasFooter) {
            notifyItemRemoved(itemCount - 1)
        }
        hasFooter = false
    }

    private fun showFooter() {
        if (hasFooter) {
            notifyItemChanged(itemCount - 1)
        } else {
            hasFooter = true
            notifyItemInserted(itemCount - 1)
        }
    }
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter) 1 else 0
    }
    override fun getItemViewType(position: Int): Int {
        return if (hasFooter && position == itemCount - 1) R.layout.sun_footer else R.layout.sun_cell
    }
    class SunMyViewHolder(var sunHolder_View : View) : RecyclerView.ViewHolder(sunHolder_View){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.sun_cell -> PhotoViewHolder.newInstance(parent).also { holder ->
                holder.itemView.setOnClickListener {
                    Bundle().apply {
                        putInt("PHOTO_POSITION", holder.adapterPosition)
                        holder.itemView.findNavController()
                            .navigate(R.id.action_oneFragment_to_pagerViewFragment, this)
                    }
                }
            }
            else -> FooterViewHolder.newInstance(parent).also {
                it.itemView.setOnClickListener {
                    sunMyViewModel.sun_retry()
                }
            }
        }
    }
    /**
     * onBindViewHolder 为每一项赋值
     * 负责将每个子项 holder 绑定数据
     * 运行多次
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //打印出所有的图片地址
        //println("Jessice:Picture----"+getItem(position)?.webformatURL)
        //println("Jessice-Sun-onBindViewHolder————"+position);
//        var imgUrl = getItem(position)?.webformatURL
//        //给图片(占位符)设置一个高度
//        holder.itemView.imageView3.layoutParams.height = getItem(position)?.webformatHeight!!

        when (holder.itemViewType) {
            R.layout.sun_footer -> (holder as FooterViewHolder).bindWithNetworkStatus(
                networkStatus
            )
            else -> {
                val photoItem = getItem(position) ?: return
                (holder as PhotoViewHolder).bindWithPhotoItem(photoItem)
            }
        }
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
class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): FooterViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sun_footer, parent, false)
            (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            return FooterViewHolder(view)
        }
    }

    fun bindWithNetworkStatus(networkStatus: NetworkStatus?) {
        with(itemView) {
            when (networkStatus) {
                NetworkStatus.FAILED -> {
                    sun_footer_TextViewid.text = "点击重试"
                    sun_footer_ProgressBarid.visibility = View.GONE
                    isClickable = true
                }
                NetworkStatus.COMPLETED -> {
                    sun_footer_TextViewid.text = "加载完毕"
                    sun_footer_ProgressBarid.visibility = View.GONE
                    isClickable = false
                }
                else -> {
                    sun_footer_TextViewid.text = "正在加载"
                    sun_footer_ProgressBarid.visibility = View.VISIBLE
                    isClickable = false
                }
            }
        }
    }
}
class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    companion object {
        fun newInstance(parent: ViewGroup): PhotoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sun_cell, parent, false)
            return PhotoViewHolder(view)
        }
    }

    fun bindWithPhotoItem(photoItem: SunUser) {

        var imgUrl = photoItem.webformatURL;
        //给图片(占位符)设置一个高度
        itemView.imageView3.layoutParams.height = photoItem.webformatHeight!!
        /**
         * 使用Glide 加载图片赋值给 holder.itemView.imageView3
         */
        Glide.with(itemView)
            .load(imgUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .listener(object : RequestListener<Drawable>{  //说明：这里的 Drawable 固定就好，一个类
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    /**
                     * 图片加载失败时执行这里
                     */
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    /**
                     * 图片加载成功时执行这里
                     */
                    return false.also {
                        //shimmerLayout?.stopShimmerAnimation()
                    }
                }
            })
            .into(itemView.imageView3)
    }
}