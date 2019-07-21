package com.example.demoyoutubeapi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoyoutubeapi.PlaylistActivity
import com.example.demoyoutubeapi.R
import com.example.demoyoutubeapi.data.InfoItem
import kotlinx.android.synthetic.main.layout_item_playlist.view.*


class PlaylistAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items:MutableList<InfoItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoViewModel(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_playlist,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as InfoViewModel).bind(items[position])
    }

    fun setData(list: List<InfoItem>) {
        items.clear()
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addMoreData(list: List<InfoItem>?){
        val currCount = items.size
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
            notifyItemRangeInserted(currCount, list.size)
        }
    }

    class InfoViewModel(view: View) : RecyclerView.ViewHolder(view) {
        private var titleTextView: TextView = itemView.titlePlaylist

        private var videoThumbnails: ImageView  = itemView.thumbnailPlaylist

        fun bind(item: InfoItem){
            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(p0: View?) {
                    var intent = Intent(itemView.context,PlaylistActivity::class.java)
                    intent.putExtra("ID",item.id)
                    itemView.context.startActivity(intent)
                }

            })
            titleTextView.text = item.snippet.title
            Glide.with(itemView.context)
                .load(item.snippet.thumbnails.default.url)
                .into(videoThumbnails)
        }
    }

}