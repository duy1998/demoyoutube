package com.example.demoyoutubeapi.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoyoutubeapi.Constant
import com.example.demoyoutubeapi.R
import com.example.demoyoutubeapi.Retrofit
import com.example.demoyoutubeapi.UserServices
import com.example.demoyoutubeapi.playlistitem.Items
import com.example.demoyoutubeapi.playlistitem.PlaylistItemResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_item_playlistitem.view.*
import okhttp3.ResponseBody
import retrofit2.Response


class PlaylistItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items:MutableList<Items> = mutableListOf()

    private val service = Retrofit.instance.create(UserServices::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlaylistItemViewModel(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_item_playlistitem,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PlaylistItemViewModel).bind(items[position])
    }

    fun setData(list: List<Items>) {
        items.clear()
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addMoreData(list: List<Items>?){
        val currCount = items.size
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
            notifyItemRangeInserted(currCount, list.size)
        }
    }

    inner class PlaylistItemViewModel(view: View) : RecyclerView.ViewHolder(view) {
        private var titleTextView: TextView = itemView.titlePlaylist

        private var videoThumbnails: ImageView  = itemView.avatarImage

        private var deleteButton: Button = itemView.deleteButton

        fun bind(item: Items){

            titleTextView.text = item.snippet.title
            Glide.with(itemView.context)
                .load(item.snippet.thumbnails.default.url)
                .into(videoThumbnails)
            Log.d("TAG",item.id)

            deleteButton.setOnClickListener {
                service.deletePlaylistItems("Bearer "+Constant.accessToken,"application/json",item.id,Constant.API_KEY)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Response<Void>> {
                        lateinit var disposable: Disposable
                        override fun onComplete() {
                            disposable.dispose()
                        }

                        override fun onSubscribe(d: Disposable) {
                            disposable = d
                        }

                        override fun onNext(t: Response<Void>) {
                            Log.d("TAG", "Success")
                        }

                        override fun onError(e: Throwable) {
                            Log.d("TAG", e.toString())
                        }
                    })
            }
        }
    }

}